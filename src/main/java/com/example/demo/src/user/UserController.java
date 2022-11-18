package com.example.demo.src.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.*;

@RestController
@RequestMapping("/app/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;




    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    // Body
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq) {
        try{
            if(postUserReq.getId().length() == 0 || postUserReq.getPw().length() == 0 || postUserReq.getNickName().length() == 0){
                return new BaseResponse<>(REQUEST_ERROR); // 2000 : 입력값 전체가 빈 값일 때
            }
            if(!isRegexPassword(postUserReq.getPw())){
                return new BaseResponse<>(POST_USERS_INVALID_PASSWORD); // 2011 : 비밀번호 기본 표현식 예외
            }
            if(!isRegexNickName(postUserReq.getNickName())){
                return new BaseResponse<>(POST_USERS_INVALID_NICK_NAME); // 2012 : 닉네임 기본 표현식 예외
            }
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

//    @ResponseBody
//    @GetMapping("/kakao")
//    public void  kakaoCallback(@RequestParam String code) throws BaseException {
//        String access_token = userService.getKaKaoAccessToken(code);
//        userService.createKakaoUser(access_token);
//    }
//-----------------------------------------------------------------------------------------------
    /**
     * 회원 1명 조회 API
     * [GET] /users/:userIdx
     * @return BaseResponse<GetUserRes>
     */
    // Path-variable
//    @ResponseBody
//    @GetMapping("/{userIdx}") // (GET) 127.0.0.1:9000/app/users/:userIdx
//    public BaseResponse<GetUserRes> getUser(@PathVariable("userIdx") int userIdx) {
//        // Get Users
//        try{
//            GetUserRes getUserRes = userProvider.getUser(userIdx);
//            return new BaseResponse<>(getUserRes);
//        } catch(BaseException exception){
//            return new BaseResponse<>((exception.getStatus()));
//        }
//    }
    /**
     * 회원 1명 필수정보 조회 API
     * [GET] /users/:userIdx/essential
     * @return BaseResponse<GetUserRes>
     */

    // Path-variable
    @ResponseBody
    @GetMapping("/essential") // (GET) localhost:9001/app/users/essential
    public BaseResponse<GetEssentialInfoRes> getUserEssential(){

        // Get Users
        try{
            int userIdx = jwtService.getUserIdx();
            GetEssentialInfoRes getUserRes = userProvider.getUserEssential(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/additive") // (GET) localhost:9001/app/users/additive
    public BaseResponse<GetAdditiveInfoRes> getUserAdditive(){
        try{
            int userIdx = jwtService.getUserIdx();
            GetAdditiveInfoRes getUserRes = userProvider.getUserAdditive(userIdx);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 로그인 API
     * [POST] /users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestBody PostLoginReq postLoginReq){
        try{
            if(postLoginReq.getId().length() == 0 || postLoginReq.getPw().length() == 0){
                return new BaseResponse<>(REQUEST_ERROR); // 2000 : 입력값 전체가 빈 값일 때
            }
            userProvider.login(postLoginReq);
            PostLoginRes postLoginRes = userProvider.login(postLoginReq);
            return new BaseResponse<>(postLoginRes);
        } catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 유저정보변경 API
     * [PATCH] /users/:userIdx
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/inactive") //status 0으로 변경 api -> 회원탈퇴
    public BaseResponse<String> inactiveUserStatus(){
        try {
            int userIdx = jwtService.getUserIdx();
            userService.inactiveUserStatus(userIdx);

            String result = "회원탈퇴완료!";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    @ResponseBody
    @PatchMapping("/password") //패스워드 변경 api
    public BaseResponse<String> modifyUserPassword(@RequestBody PatchPwReq patchPwReq){
        try {
            int userIdx = jwtService.getUserIdx();
            userService.modifyUserPassword(userIdx,patchPwReq);

            String result = "변경완료!";
            return new BaseResponse<>(result);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


}
