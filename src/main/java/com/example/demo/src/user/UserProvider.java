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

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public PostLoginRes login(PostLoginReq postLoginReq) throws BaseException{
        User user = userDao.userLogin(postLoginReq);
        String password;
        try {
            password = new AES128(Secret.USER_INFO_PASSWORD_KEY).decrypt(user.getPassword()); // 복호화 - 디비에 저장돼있는 암호를 복호화 해야 비교가 가능.
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR); // 4012 : 복호화 에러
        }

        if(postLoginReq.getPw().equals(password)){
            int userIdx = user.getUserIdx(); // response로 보낼 userId
            String jwt = jwtService.createJwt(userIdx); // jwt response
            return new PostLoginRes(userIdx,jwt,user.getGrade());
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN); // 비밀번호가 다르다면 에러메세지를 출력한다.
        }
    }

    //개인회원 전체정보 조회
    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            return userDao.getUser(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //개인회원 필수정보 조회
    public GetEssentialInfoRes getUserEssential(int userIdx) throws BaseException {
        try {
            return userDao.getUserEssential(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public GetAdditiveInfoRes getUserAdditive(int userIdx) throws BaseException {
        try {
            return userDao.getUserAdditive(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkId(String userID) throws BaseException{
        try{
            return userDao.checkId(userID);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    public int checkNickname(String nickName) throws BaseException{
        try{
            return userDao.checkNickname(nickName);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
