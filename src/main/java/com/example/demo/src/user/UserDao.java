package com.example.demo.src.user;


import com.example.demo.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

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

    //수정필요
    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select USER_ID, ID, PW, EMAIL, USER_NAME from USER where ID = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("USER_ID"),
                        rs.getString("ID"),
                        rs.getString("PW"),
                        rs.getString("USER_NAME"),
                        rs.getString("EMAIL")
                ),
                getPwdParams
                );
    }
    //수정필요
    public int loginUserStatusOn(PostLoginReq postLoginReq){
        String modifyUserPasswordQuery = "update USER set STATUS = 1 where ID = ?";
        Object[] modifyUserPasswordParams = new Object[]{postLoginReq.getId()};
        return this.jdbcTemplate.update(modifyUserPasswordQuery,modifyUserPasswordParams);
    }
}
