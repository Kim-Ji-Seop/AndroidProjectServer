package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.src.board.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class BoardProvider {
    private final BoardDao boardDao;
    private final JwtService jwtService;

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BoardProvider(BoardDao boardDao, JwtService jwtService) {
        this.boardDao = boardDao;
        this.jwtService = jwtService;
    }

    public List<GetExamSubjectListRes> getExamSubjectList(int userIdx) throws BaseException {
        try {
            return boardDao.getExamSubjectList(userIdx);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 과목 조회
    public List<GetEvaluateSubjectRes> getEvaluateSubjectList(Integer grade) throws BaseException {
        try {
            if(grade == null){
                return boardDao.getEvaluateSubjectAllList(); // 전체과목
            }else{
                return boardDao.getEvaluateSubjectGradeList(grade); // 학년별과목
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetEvaluateSubjectOneRes getEvaluateSubject(int subjectIdx) throws BaseException {
        try{
            return boardDao.getEvaluateSubject(subjectIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetEvaluateSubjectReviewRes> getEvaluateSubjectReviews(int subjectIdx) throws BaseException {
        try{
            return boardDao.getEvaluateSubjectReviews(subjectIdx);
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetCommunitiesRes> getCommunitiesList(Integer grade) throws BaseException {
        try {
            if(grade == null){
                return boardDao.getCommunitiesListAllList(); // 전체과목
            }else{
                return boardDao.getCommunitiesListGradeList(grade); // 학년별과목
            }

        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
