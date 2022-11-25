package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.board.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
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
     * @return BaseResponse<List<GetExamSubjectListRes>>
     */
    @ResponseBody
    @GetMapping("/exam-subjects") // (GET) www.seop.site/app/board/exam-subjects
    public BaseResponse<List<GetExamSubjectListRes>> getExamSubjectList(){
        // Get Users
        try{
            int userIdx = jwtService.getUserIdx();
            List<GetExamSubjectListRes> getExamSubjectListRes = boardProvider.getExamSubjectList(userIdx);
            return new BaseResponse<>(getExamSubjectListRes);
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
     * @return BaseResponse<PatchDeleteExamSubjectRes>
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

    /**
     *  과목평가 게시판 - 과목평가 조회 API
     * [GET] /evaluation-subjects
     * @return BaseResponse<List<GetEvaluateSubjectRes>>
     */
    @ResponseBody
    @GetMapping("/evaluation-subjects") // (GET) www.seop.site/app/board/evaluation-subjects
    public BaseResponse<List<GetEvaluateSubjectRes>> getEvaluateSubjectList(@RequestParam @Nullable Integer grade){
        try{
            List<GetEvaluateSubjectRes> getEvaluateSubjectRes = boardProvider.getEvaluateSubjectList(grade);
            return new BaseResponse<>(getEvaluateSubjectRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  과목평가 게시판 - 평점보기 조회 API
     * [GET] /evaluation-subjects/:subjectIdx
     * @return BaseResponse<GetEvaluateSubjectOneRes>
     */
    @ResponseBody
    @GetMapping("/evaluation-subjects/{subjectIdx}") // (GET) www.seop.site/app/board/evaluation-subjects/:subjectIdx
    public BaseResponse<GetEvaluateSubjectOneRes> getEvaluateSubject(@PathVariable("subjectIdx") int subjectIdx){
        try{
            GetEvaluateSubjectOneRes getEvaluateSubjectOneRes = boardProvider.getEvaluateSubject(subjectIdx);
            return new BaseResponse<>(getEvaluateSubjectOneRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  과목평가 게시판 - 평점보기 조회 API
     * [GET] /evaluation-subjects/reviews/:subjectIdx
     * @return BaseResponse<List<GetEvaluateSubjectReviewRes>>
     */
    @ResponseBody
    @GetMapping("/evaluation-subjects/reviews/{subjectIdx}") // (GET) www.seop.site/app/board/evaluation-subjects/reviews/:subjectIdx
    public BaseResponse<List<GetEvaluateSubjectReviewRes>> getEvaluateSubjectReviews(@PathVariable("subjectIdx") int subjectIdx){
        try{
            List<GetEvaluateSubjectReviewRes> getEvaluateSubjectReviewResList = boardProvider.getEvaluateSubjectReviews(subjectIdx);
            return new BaseResponse<>(getEvaluateSubjectReviewResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  과목평가 게시판 - 평가하기 API
     * [POST] /evaluation-subjects/reviews/:subjectIdx
     * @return BaseResponse<PostEvaluateSubjectReviewRes>
     */
    @ResponseBody
    @PostMapping("/evaluation-subjects/reviews/{subjectIdx}") // (POST) www.seop.site/app/board/evaluation-subjects/reviews/:subjectIdx
    public BaseResponse<PostEvaluateSubjectReviewRes> createEvaluateSubjectReview(@PathVariable("subjectIdx") int subjectIdx,@RequestBody PostEvaluateSubjectReviewReq postEvaluateSubjectReviewReq){
        try{
            int userIdx = jwtService.getUserIdx();
            PostEvaluateSubjectReviewRes postEvaluateSubjectReviewRes = boardService.createEvaluateSubjectReview(userIdx,subjectIdx,postEvaluateSubjectReviewReq);
            return new BaseResponse<>(postEvaluateSubjectReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
