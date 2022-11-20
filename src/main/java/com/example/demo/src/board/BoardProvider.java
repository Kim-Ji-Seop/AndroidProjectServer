package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.src.board.model.GetExamSubjectListRes;
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
}
