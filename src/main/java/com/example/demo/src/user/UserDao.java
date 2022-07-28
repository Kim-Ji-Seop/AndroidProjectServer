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
    public GetUserRes getUserEssential(int userIdx){
        String getUserQueryEss = "select ID,PW,USER_NAME,EMAIL,PHONE_NUMBER from USER where USER_ID = ?;";
        return this.jdbcTemplate.queryForObject(getUserQueryEss,
                (rs, rowNum) -> new GetUserRes(
                        rs.getString("ID"),
                        rs.getString("PW"),
                        rs.getString("USER_NAME"),
                        rs.getString("EMAIL"),
                        rs.getString("PHONE_NUMBER")),
                userIdx);
    }
    public GetUserRes getUserAdditive(int userIdx){
        String getUserAdd = "select SEX,LOGIN_KAKAO,INTRODUCE from USER where USER_ID = ?;";
        return this.jdbcTemplate.queryForObject(getUserAdd,
                (rs,rowNum) -> new GetUserRes(
                        rs.getString("SEX"),
                        rs.getInt("LOGIN_KAKAO"),
                        rs.getString("INTRODUCE")),
                userIdx);
    }
    
// createUser -> 회원가입 메소드
// checkEmail -> 중복이메일검사 메소드
    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into USER (USER_ID,ID,PW,USER_NAME,EMAIL,PHONE_NUMBER,BIRTH,SEX,LOGIN_KAKAO,INTRODUCE,CREATED_AT,UPDATED_AT,STATUS) VALUES (?,?,?,?,?,?,?,?,?,?,NOW(),NOW(),1)";
        Object[] createUserParams = new Object[]{postUserReq.getUserId(),
                                                postUserReq.getId(),
                                                postUserReq.getPw(),
                                                postUserReq.getUserName(),
                                                postUserReq.getEmail(),
                                                postUserReq.getPhoneNumber(),
                                                postUserReq.getBirth(),
                                                postUserReq.getSex(),
                                                postUserReq.getLoginKakao(),
                                                postUserReq.getIntroduce(),
                                                };
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInsertIdQuery = "select USER_ID from USER order by USER_ID desc limit 1;";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }
    public int checkId(String id){
        String checkIdQuery = "select exists(select ID from USER where ID = ?)";
        return this.jdbcTemplate.queryForObject(checkIdQuery,
                int.class,
                id);
    }
    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select EMAIL from USER where EMAIL = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public int inactiveUserStatus(PatchUserReq patchUserReq){
        String inactiveUserStatusQuery = "update USER set STATUS = 0 where USER_ID = ?";
        Object[] inactiveUserStatusParams = new Object[]{patchUserReq.getUserId()};
        return this.jdbcTemplate.update(inactiveUserStatusQuery,inactiveUserStatusParams);
    }
    public int modifyUserPassword(PatchUserReq patchUserReq){
        String modifyUserPasswordQuery = "update USER set PW = ? where USER_ID = ?";
        Object[] modifyUserPasswordParams = new Object[]{patchUserReq.getPw(), patchUserReq.getUserId()};
        return this.jdbcTemplate.update(modifyUserPasswordQuery,modifyUserPasswordParams);
    }

    //수정필요
    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select USER_ID, ID, PW, EMAIL, USER_NAME from USER where ID = ?";
        String getPwdParams = postLoginReq.getID();

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
        Object[] modifyUserPasswordParams = new Object[]{postLoginReq.getID()};
        return this.jdbcTemplate.update(modifyUserPasswordQuery,modifyUserPasswordParams);
    }
}
