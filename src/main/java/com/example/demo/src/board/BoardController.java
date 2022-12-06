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
    @GetMapping("/exam-subjects") // (GET) www.seop.site/app/boards/exam-subjects
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
    @PostMapping("/exam-subjects") // (POST) www.seop.site/app/boards/exam-subjects
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
    @PatchMapping ("/exam-subjects/{listIdx}") // (PATCH) www.seop.site/app/boards/exam-subjects/:listIdx
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
    @PatchMapping ("/exam-subjects/del/{listIdx}") // (PATCH) www.seop.site/app/boards/exam-subjects/del/:listIdx
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
    @GetMapping("/evaluation-subjects") // (GET) www.seop.site/app/boards/evaluation-subjects
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
    @GetMapping("/evaluation-subjects/{subjectIdx}") // (GET) www.seop.site/app/boards/evaluation-subjects/:subjectIdx
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
    @GetMapping("/evaluation-subjects/reviews/{subjectIdx}") // (GET) www.seop.site/app/boards/evaluation-subjects/reviews/:subjectIdx
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
    @PostMapping("/evaluation-subjects/reviews/{subjectIdx}") // (POST) www.seop.site/app/boards/evaluation-subjects/reviews/:subjectIdx
    public BaseResponse<PostEvaluateSubjectReviewRes> createEvaluateSubjectReview(@PathVariable("subjectIdx") int subjectIdx,@RequestBody PostEvaluateSubjectReviewReq postEvaluateSubjectReviewReq){
        try{
            int userIdx = jwtService.getUserIdx();
            PostEvaluateSubjectReviewRes postEvaluateSubjectReviewRes = boardService.createEvaluateSubjectReview(userIdx,subjectIdx,postEvaluateSubjectReviewReq);
            return new BaseResponse<>(postEvaluateSubjectReviewRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  커뮤니티 게시판 - 학년별 커뮤니티 게시판 조회 API
     * [GET] /communities
     * @return BaseResponse<List<GetCommunitiesRes>>
     */
    @ResponseBody
    @GetMapping("/communities") // (GET) www.seop.site/app/boards/communities
    public BaseResponse<List<GetCommunitiesRes>> getCommunitiesList(@RequestParam @Nullable Integer grade){
        try{
            List<GetCommunitiesRes> getCommunitiesResList = boardProvider.getCommunitiesList(grade);
            return new BaseResponse<>(getCommunitiesResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  커뮤니티 게시판 - 학년별 커뮤니티 게시글 수정/삭제 권한 API
     * [GET] /communities/auth
     * @return BaseResponse<GetIsAuthRes>
     */
    @ResponseBody
    @GetMapping("/communities/auth") // (GET) www.seop.site/app/boards/communities/auth
    public BaseResponse<GetIsAuthRes> getIsAuth(){
        try{
            int userIdx = jwtService.getUserIdx();
            return new BaseResponse<>(new GetIsAuthRes(userIdx));
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  커뮤니티 게시판 - 학년별 커뮤니티 게시글 작성 API
     * [POST] /communities
     * @return BaseResponse<PostCommunityRes>
     */
    @ResponseBody
    @PostMapping("/communities") // (POST) www.seop.site/app/boards/communities
    public BaseResponse<PostCommunityRes> createCommunity(@RequestBody PostCommunityReq postCommunityReq){
        try{
            int userIdx = jwtService.getUserIdx();
            PostCommunityRes postCommunityRes = boardService.createCommunity(userIdx,postCommunityReq);
            return new BaseResponse<>(postCommunityRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  커뮤니티 게시판 - 학년별 커뮤니티 게시글 수정 API
     * [PATCH] /communities/:communityIdx
     * @return BaseResponse<PatchCommunityRes>
     */
    @ResponseBody
    @PatchMapping("/communities/{communityIdx}") // (GET) www.seop.site/app/boards/communities/:communityIdx
    public BaseResponse<PatchCommunityRes> updateCommunity(@PathVariable("communityIdx") int communityIdx,@RequestBody PatchCommunityReq patchCommunityReq){
        try{
            int userIdx = jwtService.getUserIdx();
            PatchCommunityRes patchCommunityRes = boardService.updateCommunity(userIdx,communityIdx,patchCommunityReq);
            return new BaseResponse<>(patchCommunityRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  커뮤니티 게시판 - 학년별 커뮤니티 게시글 삭제 API
     * [PATCH] /communities/del/:communityIdx
     * @return BaseResponse<DeleteCommunityRes>
     */
    @ResponseBody
    @PatchMapping("/communities/del/{communityIdx}") // (GET) www.seop.site/app/boards/communities/del/:communityIdx
    public BaseResponse<DeleteCommunityRes> deleteCommunity(@PathVariable("communityIdx") int communityIdx){
        try{
            int userIdx = jwtService.getUserIdx();
            DeleteCommunityRes deleteCommunityRes = boardService.deleteCommunity(userIdx,communityIdx);
            return new BaseResponse<>(deleteCommunityRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  커뮤니티 게시판 - 학년별 커뮤니티 게시판 댓글 조회 API
     * [GET] /communities/comments/:communityIdx
     * @return BaseResponse<List<GetCommentsRes>>
     */
    @ResponseBody
    @GetMapping("/communities/comments/{communityIdx}") // (GET) www.seop.site/app/boards/communities/comments/:communityIdx
    public BaseResponse<List<GetCommentsRes>> getCommentsList(@PathVariable("communityIdx") int communityIdx){
        try{
            List<GetCommentsRes> getCommentsResList = boardProvider.getCommentsList(communityIdx);
            return new BaseResponse<>(getCommentsResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  시간표 강의목록 조회
     * [GET] /time-table/courses
     * @return BaseResponse<List<GetCoursesRes>>
     */
    @ResponseBody
    @GetMapping("/time-table/courses") // (GET) www.seop.site/app/boards/time-table/courses
    public BaseResponse<List<GetCoursesRes>> getCoursesList(@RequestParam @Nullable Integer grade){
        try{
            List<GetCoursesRes> getCoursesResList = boardProvider.getCoursesList(grade);
            return new BaseResponse<>(getCoursesResList);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  시간표 강의 추가
     * [POST] /time-table/courses/{courseIdx}
     * @return BaseResponse<PostCourseRes>
     */
    @ResponseBody
    @PostMapping("/time-table/courses/{courseIdx}") // (GET) www.seop.site/app/boards/time-table/courses/:courseIdx
    public BaseResponse<PostCourseRes> createCourse(@PathVariable("courseIdx") int courseIdx){
        try{
            int userIdx = jwtService.getUserIdx();
            PostCourseRes postCourseRes = boardService.createCourse(userIdx,courseIdx);
            return new BaseResponse<>(postCourseRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
