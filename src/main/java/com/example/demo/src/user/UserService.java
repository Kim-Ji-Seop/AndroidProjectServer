package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;

import static com.example.demo.config.BaseResponseStatus.*;

// Service -> Create, Update, Delete 의 로직 처리
// post 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;
    }

    //POST
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        //중복 이메일 체크
        if(userProvider.checkEmail(postUserReq.getEMAIL()) ==1){
            throw new BaseException(POST_USERS_EXISTS_EMAIL);
        }
        //중복 아이디 체크
        if(userProvider.checkId(postUserReq.getID()) ==1){
            throw new BaseException(POST_USERS_EXISTS_ID);
        }



        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(postUserReq.getPW());
            postUserReq.setPW(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int userIdx = userDao.createUser(postUserReq);
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(
                    jwt,
                    userIdx,
                    postUserReq.getID(),
                    postUserReq.getPW(),
                    postUserReq.getUSER_NAME(),
                    postUserReq.getEMAIL(),
                    postUserReq.getPHONE_NUMBER(),
                    postUserReq.getBIRTH(),
                    postUserReq.getSEX(),
                    postUserReq.getACCOUNT(),
                    postUserReq.getADDRESS(),
                    postUserReq.getLOGIN_KAKAO(),
                    postUserReq.getINTRODUCE(),
                    postUserReq.getCREATED_AT(),
                    postUserReq.getUPDATED_AT(),
                    postUserReq.getSTATUS()
                    );
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void inactiveUserStatus(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.inactiveUserStatus(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERSTATUS);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserPassword(PatchUserReq patchUserReq) throws BaseException {
        try{
            int result = userDao.modifyUserPassword(patchUserReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERPASSWORD);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
