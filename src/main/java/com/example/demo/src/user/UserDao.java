package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Locale;

import static com.example.demo.config.BaseResponseStatus.*;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


//    public List<GetUserRes> getUsersByEmail(String email){
//        String getUsersByEmailQuery = "select * from USER where EMAIL =?";
//        String getUsersByEmailParams = email;
//        return this.jdbcTemplate.query(getUsersByEmailQuery,
//                (rs, rowNum) -> new GetUserRes(
//                        rs.getInt("USER_ID"),
//                        rs.getString("ID"),
//                        rs.getString("PW"),
//                        rs.getString("USER_NAME"),
//                        rs.getString("EMAIL"),
//                        rs.getString("PHONE_NUMBER"),
//                        rs.getDate("BIRTH"),
//                        rs.getString("SEX"),
//                        rs.getString("ACCOUNT"),
//                        rs.getString("ADDRESS"),
//                        rs.getInt("LOGIN_KAKAO"),
//                        rs.getString("INTRODUCE"),
//                        rs.getTimestamp("CREATED_AT"),
//                        rs.getTimestamp("UPDATED_AT"),
//                        rs.getInt("STATUS")),
//                getUsersByEmailParams);
//    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from USER where USER_ID = ?";
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("ID"),
                        rs.getString("PW"),
                        rs.getString("USER_NAME"),
                        rs.getString("EMAIL"),
                        rs.getString("PHONE_NUMBER"),
                        rs.getDate("BIRTH"),
                        rs.getString("SEX"),
                        rs.getInt("LOGIN_KAKAO"),
                        rs.getString("INTRODUCE"),
                        rs.getDate("CREATED_AT")),
                userIdx);
    }
    public GetEssentialInfoRes getUserEssential(int userIdx){
        String getUserQueryEss = "select USER_NAME,EMAIL,PHONE_NUMBER from USER where USER_ID = ?;";
        return this.jdbcTemplate.queryForObject(getUserQueryEss,
                (rs, rowNum) -> new GetEssentialInfoRes(
                        rs.getString("USER_NAME"),
                        rs.getString("EMAIL"),
                        rs.getString("PHONE_NUMBER")),
                userIdx);
    }
    public GetAdditiveInfoRes getUserAdditive(int userIdx){
        String getUserAdd = "select BIRTH,SEX,LOGIN_KAKAO,INTRODUCE from USER where USER_ID = ?;";
        return this.jdbcTemplate.queryForObject(getUserAdd,
                (rs,rowNum) -> new GetAdditiveInfoRes(
                        rs.getDate("BIRTH"),
                        rs.getString("SEX"),
                        rs.getInt("LOGIN_KAKAO"),
                        rs.getString("INTRODUCE")),
                userIdx);
    }
    
// createUser -> 회원가입 메소드
// checkEmail -> 중복이메일검사 메소드
    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into user (userID,password,nickname,department,grade) values (?,?,?,?,?)";
        Object[] createUserParams = new Object[]{
                                                postUserReq.getId(),
                                                postUserReq.getPw(),
                                                postUserReq.getNickName(),
                                                postUserReq.getDepartment(),
                                                postUserReq.getGrade()
                                                };
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    // 회원가입 아이디 중복 확인
    public int checkId(String userID){
        String checkIdQuery = "select exists(select id from user where userID = ? and status = 'A')";
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                userID);
    }

    // 회원가입 닉네임 중복 확인
    public int checkNickname(String nickName){
        String checkIdQuery = "select exists(select id from user where nickname = ? and status = 'A')";
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                nickName);
    }

    public int inactiveUserStatus(int userIdx){
        String inactiveUserStatusQuery = "update USER set STATUS = 0 where USER_ID = ?";
        Object[] inactiveUserStatusParams = new Object[]{userIdx};
        return this.jdbcTemplate.update(inactiveUserStatusQuery,inactiveUserStatusParams);
    }
    public int modifyUserPassword(int userIdx,PatchPwReq patchPwReq){
        String modifyUserPasswordQuery = "update USER set PW = ? where USER_ID = ?";
        Object[] modifyUserPasswordParams = new Object[]{patchPwReq.getPw(), userIdx};
        return this.jdbcTemplate.update(modifyUserPasswordQuery,modifyUserPasswordParams);
    }

//    //수정필요
//    public User getPwd(PostLoginReq postLoginReq){
//        String getPwdQuery = "select USER_ID, ID, PW, EMAIL, USER_NAME from USER where ID = ?";
//        String getPwdParams = postLoginReq.getId();
//
//        return this.jdbcTemplate.queryForObject(getPwdQuery,
//                (rs,rowNum)-> new User(
//                        rs.getInt("USER_ID"),
//                        rs.getString("ID"),
//                        rs.getString("PW"),
//                        rs.getString("USER_NAME"),
//                        rs.getString("EMAIL")
//                ),
//                getPwdParams
//                );
//    }
    @Transactional(rollbackFor = {Exception.class})
    public User userLogin(PostLoginReq postLoginReq) throws BaseException {
        //암호화된 비밀번호를 params로 넣어준다
        String pwdParams;
        try{
            pwdParams=new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postLoginReq.getPw());// 로그인 시 request받은 비밀번호 암호화 -> 회원 검색을 위함
            System.out.println(pwdParams);
        }catch (Exception ignored) {
            System.out.println(ignored);
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }
        // 패스워드 일치 검사
        if(loginNotPassword(pwdParams,postLoginReq) == 1){
            throw new BaseException(FAILED_TO_LOGIN_PASSWORD); // 비밀번호 틀림 3015 오류코드
        }
        // 로그인 전체정보 일치 검사
        if(loginFlag(pwdParams,postLoginReq) == 0){
            throw new BaseException(POST_USERS_NOT_FOUND); // 일치하는 회원정보 없을 때 2021 오류코드
        }
        //일치 검사를 다 마치면 select해온다.
        String getLogInQuery =
                "select id,userID,password,nickname,department,grade from user where userID = ? and password = ?";


        Object[] logInParams = new Object[]{ postLoginReq.getId(), pwdParams };

        return this.jdbcTemplate.queryForObject(getLogInQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("id"),
                        rs.getString("userID"),
                        rs.getString("password"),
                        rs.getString("nickname"),
                        rs.getString("department"),
                        rs.getInt("grade")
                ), logInParams// RowMapper(위의 링크 참조): 원하는 결과값 형태로 받기
        ); // 한 개의 회원정보를 얻기 위한 jdbcTemplate 함수(Query, 객체 매핑 정보, Params)의 결과 반환
    }

    public int loginNotPassword(String pwdParams,PostLoginReq postLoginReq){
        String loginCheckPasswordQuery =
                "select exists(select id\n" +
                        "        from user\n" +
                        "        where userID=? and password != ?)";
        return this.jdbcTemplate.queryForObject(loginCheckPasswordQuery,
                int.class,
                postLoginReq.getId(),pwdParams);
    }
    public int loginFlag(String pwdParams,PostLoginReq postLoginReq){
        String loginCheckQuery =
                "select exists(select id\n" +
                        "        from user\n" +
                        "        where userID = ? and password = ?)";
        return this.jdbcTemplate.queryForObject(loginCheckQuery,
                int.class,
                postLoginReq.getId(),pwdParams);
    }
//    //수정필요
//    public int loginUserStatusOn(PostLoginReq postLoginReq){
//        String modifyUserPasswordQuery = "update USER set STATUS = 1 where ID = ?";
//        Object[] modifyUserPasswordParams = new Object[]{postLoginReq.getId()};
//        return this.jdbcTemplate.update(modifyUserPasswordQuery,modifyUserPasswordParams);
//    }
}
