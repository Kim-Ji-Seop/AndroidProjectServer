package com.example.demo.src.user;



import com.example.demo.config.BaseException;
import com.example.demo.config.secret.Secret;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.AES128;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public PostUserRes createUser(PostUserReq postUserReq) throws BaseException {
        String pwd;
        // 중복 아이디 체크
        if(userProvider.checkId(postUserReq.getId()) ==1){
            throw new BaseException(POST_USERS_EXISTS_ID); // 2018 : 중복 아이디
        }
        // 중복 닉네임 체크
        if(userProvider.checkNickname(postUserReq.getNickName())==1){
            throw new BaseException(POST_USERS_EXISTS_NICK_NAME); // 2019 : 중복 닉네임
        }
        try{
            // 비밀번호 암호화
            pwd = new AES128(Secret.USER_INFO_PASSWORD_KEY).encrypt(postUserReq.getPw()); // 비밀번호 암호화
            postUserReq.setPw(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            // 유저 고유식별번호
            int userIdx = userDao.createUser(postUserReq);
            String jwt = jwtService.createJwt(userIdx);
            return new PostUserRes(jwt, userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void inactiveUserStatus(int userIdx) throws BaseException {
        try{
            int result = userDao.inactiveUserStatus(userIdx);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERSTATUS_OFF);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public void modifyUserPassword(int userIdx,PatchPwReq patchPwReq) throws BaseException {
        String pwd;
        try{
            //암호화
            pwd = new SHA256().encrypt(patchPwReq.getPw());
            patchPwReq.setPw(pwd);

        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_ENCRYPTION_ERROR);
        }
        try{
            int result = userDao.modifyUserPassword(userIdx,patchPwReq);
            if(result == 0){
                throw new BaseException(MODIFY_FAIL_USERPASSWORD);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
