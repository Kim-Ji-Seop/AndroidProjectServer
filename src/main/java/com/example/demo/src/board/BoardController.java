package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.board.model.GetExamSubjectListRes;
import com.example.demo.src.user.model.GetEssentialInfoRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/app/boards")
public class BoardController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BoardProvider boardProvider;
    @Autowired
    private final BoardService boardService;
    @Autowired
    private final JwtService jwtService;

    public BoardController(BoardProvider boardProvider, BoardService boardService, JwtService jwtService) {
        this.boardProvider = boardProvider;
        this.boardService = boardService;
        this.jwtService = jwtService;
    }

    /**
     *  시험과제게시판 목록 조회API
     * [GET] /exam-subjects
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @GetMapping("/exam-subjects") // (GET) localhost:9001/app/users/essential
    public BaseResponse<List<GetExamSubjectListRes>> getExamSubjectList(){

        // Get Users
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetExamSubjectListRes> getEssentialInfoRes = boardProvider.getExamSubjectList(userIdx);
            return new BaseResponse<>(getEssentialInfoRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
