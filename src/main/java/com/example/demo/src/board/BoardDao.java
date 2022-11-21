package com.example.demo.src.board;

import com.example.demo.src.board.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class BoardDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<GetExamSubjectListRes> getExamSubjectList(int userIdx){
        String query = "select id,title,date_format(endAt, '%m/%d') as endAt from exam_sub_board where status='A' and userIdx = ?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetExamSubjectListRes(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("endAt")
                ),userIdx);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PostExamSubjectRes createExamSubject(int userIdx, PostExamSubjectReq postExamSubjectReq) {
        String query = "insert into exam_sub_board (title,content,endAt,userIdx) values (?,?,?,?)";
        Object[] params = new Object[]{
                postExamSubjectReq.getTitle(),
                postExamSubjectReq.getContent(),
                postExamSubjectReq.getEndAt(),
                userIdx
        };
        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";

        int lastInsertId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        String selectQuery = "select id,userIdx,title,content,endAt from exam_sub_board where id = ?";
        return this.jdbcTemplate.queryForObject(selectQuery,
                (rs,rowNum)-> new PostExamSubjectRes(
                        rs.getInt("id"),
                        rs.getInt("userIdx"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("endAt")
                ),lastInsertId);
    }
    @Transactional(rollbackFor = {Exception.class})
    public PatchExamSubjectRes refactorExamSubject(int userIdx, int listIdx, PatchExamSubjectReq patchExamSubjectReq) {
        String query = "update exam_sub_board set title=?,content=?,endAt=? " +
                "where id=? and userIdx=?";
        Object[] params = new Object[]{
                patchExamSubjectReq.getTitle(),
                patchExamSubjectReq.getContent(),
                patchExamSubjectReq.getEndAt(),
                listIdx,
                userIdx
        };
        this.jdbcTemplate.update(query, params);

        // 방금 수정한 정보 조회
        String responseQuery = "select id,title,content,endAt from exam_sub_board where id=? and userIdx=?";
        return this.jdbcTemplate.queryForObject(responseQuery,
                (rs, rowNum) -> new PatchExamSubjectRes(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("endAt")),
                listIdx,userIdx);
    }
    @Transactional(rollbackFor = {Exception.class})
    public PatchDeleteExamSubjectRes deleteExamSubject(int userIdx, int listIdx) {
        String query = "update exam_sub_board set status='D' " +
                "where id=? and userIdx=?";

        this.jdbcTemplate.update(query, listIdx,userIdx);
        // 방금 삭제한 정보 조회
        String responseQuery = "select id,status from exam_sub_board where id=? and userIdx=?";
        return this.jdbcTemplate.queryForObject(responseQuery,
                (rs, rowNum) -> new PatchDeleteExamSubjectRes(
                        rs.getInt("id"),
                        rs.getString("status")),
                listIdx,userIdx);
    }
}
