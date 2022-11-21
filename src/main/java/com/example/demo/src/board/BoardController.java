package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.board.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.PATCH_EXAM_SUB_TITLE_ISEMPTY;
import static com.example.demo.config.BaseResponseStatus.POST_EXAM_SUB_TITLE_ISEMPTY;

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
    @GetMapping("/exam-subjects") // (GET) www.seop.site/app/board/exam-subjects
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

    /**
     *  시험/과제 게시판 - 시험/과제 추가 API
     * [POST] /exam-subjects
     * @return BaseResponse<PostExamSubjectRes>
     */
    @ResponseBody
    @PostMapping("/exam-subjects") // (POST) www.seop.site/app/board/exam-subjects
    public BaseResponse<PostExamSubjectRes> createExamSubject(@RequestBody PostExamSubjectReq postExamSubjectReq){
        try{
            // 형식적 Validation
            if(postExamSubjectReq.getTitle().length() == 0){
                throw new BaseException(POST_EXAM_SUB_TITLE_ISEMPTY); // 2022 : 제목을 등록해주세요.
            }

            int userIdx = jwtService.getUserIdx();
            PostExamSubjectRes createExamSubjectRes = boardService.createExamSubject(userIdx,postExamSubjectReq);
            return new BaseResponse<>(createExamSubjectRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *  시험/과제 게시판 - 시험/과제 수정 API
     * [PATCH] /exam-subjects/:listIdx
     * @return BaseResponse<PatchExamSubjectRes>
     */
    @ResponseBody
    @PatchMapping ("/exam-subjects/{listIdx}") // (PATCH) www.seop.site/app/board/exam-subjects/:listIdx
    public BaseResponse<PatchExamSubjectRes> refactorExamSubject(@PathVariable("listIdx") int listIdx, @RequestBody PatchExamSubjectReq patchExamSubjectReq){
        try{
            // 형식적 Validation
            if(patchExamSubjectReq.getTitle().length() == 0){
                throw new BaseException(PATCH_EXAM_SUB_TITLE_ISEMPTY); // 2023 : 제목을 등록해주세요.
            }

            int userIdx = jwtService.getUserIdx();
            PatchExamSubjectRes refactorExamSubjectRes = boardService.refactorExamSubject(userIdx,listIdx,patchExamSubjectReq);
            return new BaseResponse<>(refactorExamSubjectRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  시험/과제 게시판 - 시험/과제 삭제 API
     * [PATCH] /exam-subjects/del/:listIdx
     * @return BaseResponse<PatchExamSubjectRes>
     */
    @ResponseBody
    @PatchMapping ("/exam-subjects/del/{listIdx}") // (PATCH) www.seop.site/app/board/exam-subjects/del/:listIdx
    public BaseResponse<PatchDeleteExamSubjectRes> deleteExamSubject(@PathVariable("listIdx") int listIdx){
        try{
            int userIdx = jwtService.getUserIdx();
            PatchDeleteExamSubjectRes deleteExamSubjectRes = boardService.deleteExamSubject(userIdx,listIdx);
            return new BaseResponse<>(deleteExamSubjectRes);
        }catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
