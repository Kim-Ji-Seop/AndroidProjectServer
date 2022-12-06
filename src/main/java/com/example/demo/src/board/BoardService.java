package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.src.board.model.*;
import com.example.demo.src.user.model.PostUserRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BoardService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final BoardDao boardDao;
    private final BoardProvider boardProvider;
    private final JwtService jwtService;

    public BoardService(BoardDao boardDao, BoardProvider boardProvider, JwtService jwtService) {
        this.boardDao = boardDao;
        this.boardProvider = boardProvider;
        this.jwtService = jwtService;
    }

    // 시험/과제 게시판 - 시험/과제 추가
    public PostExamSubjectRes createExamSubject(int userIdx, PostExamSubjectReq postExamSubjectReq) throws BaseException {
        try{
            return boardDao.createExamSubject(userIdx,postExamSubjectReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR); // 4000
        }
    }

    // 시험/과제 게시판 - 시험/과제 수정
    public PatchExamSubjectRes refactorExamSubject(int userIdx, int listIdx, PatchExamSubjectReq patchExamSubjectReq) throws BaseException {
        try{
            return boardDao.refactorExamSubject(userIdx,listIdx,patchExamSubjectReq);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR); // 4000
        }
    }

    public PatchDeleteExamSubjectRes deleteExamSubject(int userIdx, int listIdx) throws BaseException {
        try{
            return boardDao.deleteExamSubject(userIdx,listIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostEvaluateSubjectReviewRes createEvaluateSubjectReview(int userIdx, int subjectIdx, PostEvaluateSubjectReviewReq postEvaluateSubjectReviewReq) throws BaseException {
        try{
            return boardDao.createEvaluateSubjectReview(userIdx,subjectIdx,postEvaluateSubjectReviewReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostCommunityRes createCommunity(int userIdx, PostCommunityReq postCommunityReq) throws BaseException {
        try{
            return boardDao.createCommunity(userIdx,postCommunityReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PatchCommunityRes updateCommunity(int userIdx, int communityIdx, PatchCommunityReq patchCommunityReq) throws BaseException {
        try{
            return boardDao.updateCommunity(userIdx,communityIdx,patchCommunityReq);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public DeleteCommunityRes deleteCommunity(int userIdx, int communityIdx) throws BaseException {
        try{
            return boardDao.deleteCommunity(userIdx,communityIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostCourseRes createCourse(int userIdx, int courseIdx) throws BaseException {
        try{
            return boardDao.createCourse(userIdx,courseIdx);
        }catch (BaseException exception){
            throw new BaseException((exception.getStatus()));
        }
    }
}
