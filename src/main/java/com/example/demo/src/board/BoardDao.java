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
        String query = "select id,title,content,endAt from exam_sub_board where status='A' and userIdx = ?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetExamSubjectListRes(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getDate("endAt")
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
        String responseQuery = "select id,userIdx,title,content,endAt from exam_sub_board where id=? and userIdx=?";
        return this.jdbcTemplate.queryForObject(responseQuery,
                (rs, rowNum) -> new PatchExamSubjectRes(
                        rs.getInt("id"),
                        rs.getInt("userIdx"),
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

    // 전체학년 과목 조회
    public List<GetEvaluateSubjectRes> getEvaluateSubjectAllList() {
        String query =
                "select id,grade,subjectName,professor\n" +
                "from evaluate_sub_board\n" +
                "where status = 'A'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetEvaluateSubjectRes(
                        rs.getInt("id"),
                        rs.getInt("grade"),
                        rs.getString("subjectName"),
                        rs.getString("professor")
                )
        );

    }

    // 학년별 과목 조회
    public List<GetEvaluateSubjectRes> getEvaluateSubjectGradeList(Integer grade) {
        String query =
                "select id,grade,subjectName,professor\n" +
                        "from evaluate_sub_board\n" +
                        "where status = 'A' and grade = ?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetEvaluateSubjectRes(
                        rs.getInt("id"),
                        rs.getInt("grade"),
                        rs.getString("subjectName"),
                        rs.getString("professor")
                ), grade);
    }

    public GetEvaluateSubjectOneRes getEvaluateSubject(int subjectIdx) {
        String query =
                "select esb.id as id,round(avg(score),1) as scoreAverage\n" +
                "from sub_review\n" +
                "inner join evaluate_sub_board esb on sub_review.subjectID = esb.id\n" +
                "where esb.id = ?";
        return this.jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> new GetEvaluateSubjectOneRes(
                        rs.getInt("id"),
                        rs.getFloat("scoreAverage")
                ),subjectIdx);
    }

    public List<GetEvaluateSubjectReviewRes> getEvaluateSubjectReviews(int subjectIdx) {
        String query =
                "select content,score\n" +
                        "from sub_review\n" +
                        "inner join evaluate_sub_board esb on sub_review.subjectID = esb.id\n" +
                        "where esb.id = ?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetEvaluateSubjectReviewRes(
                        rs.getString("content"),
                        rs.getFloat("score")
                ),subjectIdx);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PostEvaluateSubjectReviewRes createEvaluateSubjectReview(int userIdx, int subjectIdx, PostEvaluateSubjectReviewReq postEvaluateSubjectReviewReq) {
        String query = "insert into sub_review (content,userIdx,subjectID,score) values (?,?,?,?)";
        Object[] params = new Object[]{
                postEvaluateSubjectReviewReq.getContent(),
                userIdx,
                subjectIdx,
                postEvaluateSubjectReviewReq.getScore()
        };
        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";

        int lastInsertId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        String selectQuery = "select content,score from sub_review where id = ?";
        return this.jdbcTemplate.queryForObject(selectQuery,
                (rs,rowNum)-> new PostEvaluateSubjectReviewRes(
                        rs.getString("content"),
                        rs.getFloat("score")
                ),lastInsertId);
    }
    //
    public List<GetCommunitiesRes> getCommunitiesListAllList() {
        String query =
                "select cb.id,cb.userIdx,u.grade,title,content,date_format(cb.createdAt, '%Y.%m.%d') as createdAt\n" +
                        "from community_board cb\n" +
                        "inner join user u on cb.userIdx = u.id\n" +
                        "where cb.status='A'"+
                        "order by cb.updatedAt desc";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetCommunitiesRes(
                        rs.getInt("id"),
                        rs.getInt("userIdx"),
                        rs.getInt("grade"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("createdAt"))
        );

    }

    //
    public List<GetCommunitiesRes> getCommunitiesListGradeList(Integer grade) {
        String query =
                "select cb.id,cb.userIdx,u.grade,title,content,date_format(cb.createdAt, '%Y.%m.%d') as createdAt\n" +
                        "from community_board cb\n" +
                        "inner join user u on cb.userIdx = u.id\n" +
                        "where u.grade = ? and cb.status = 'A' order by cb.updatedAt desc";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetCommunitiesRes(
                        rs.getInt("id"),
                        rs.getInt("userIdx"),
                        rs.getInt("grade"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("createdAt")), grade);
    }
    @Transactional(rollbackFor = {Exception.class})
    public PostCommunityRes createCommunity(int userIdx, PostCommunityReq postCommunityReq) {
        String query = "insert into community_board (title,content,userIdx) values (?,?,?)";
        Object[] params = new Object[]{
                postCommunityReq.getTitle(),
                postCommunityReq.getContent(),
                userIdx
        };
        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";

        int lastInsertId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        String selectQuery = "select cb.userIdx,u.grade,title,content\n" +
                                "from community_board cb\n" +
                                "inner join user u on cb.userIdx = u.id\n" +
                                "where cb.id = ?";
        return this.jdbcTemplate.queryForObject(selectQuery,
                (rs,rowNum)-> new PostCommunityRes(
                        rs.getInt("userIdx"),
                        rs.getInt("grade"),
                        rs.getString("title"),
                        rs.getString("content")
                ),lastInsertId);
    }

    @Transactional(rollbackFor = {Exception.class})
    public PatchCommunityRes updateCommunity(int userIdx, int communityIdx, PatchCommunityReq patchCommunityReq) {
        String query = "update community_board set title = ?, content = ? where id=? and userIdx=?";
        Object[] params = new Object[]{
                patchCommunityReq.getTitle(),
                patchCommunityReq.getContent(),
                communityIdx,
                userIdx
        };
        this.jdbcTemplate.update(query, params);

        // 방금 수정한 정보 조회
        String responseQuery = "select id,userIdx,title,content from community_board where id=? and userIdx=?";
        return this.jdbcTemplate.queryForObject(responseQuery,
                (rs, rowNum) -> new PatchCommunityRes(
                        rs.getInt("id"),
                        rs.getInt("userIdx"),
                        rs.getString("title"),
                        rs.getString("content")),
                communityIdx,userIdx);
    }

    @Transactional(rollbackFor = {Exception.class})
    public DeleteCommunityRes deleteCommunity(int userIdx, int communityIdx) {
        String query = "update community_board set status='D' where id=? and userIdx=?";
        Object[] params = new Object[]{
                communityIdx,
                userIdx
        };
        this.jdbcTemplate.update(query, params);

        // 방금 수정한 정보 조회
        String responseQuery = "select id,status from community_board where id=? and userIdx=?";
        return this.jdbcTemplate.queryForObject(responseQuery,
                (rs, rowNum) -> new DeleteCommunityRes(
                        rs.getInt("id"),
                        rs.getString("status")),
                communityIdx,userIdx);
    }

    public List<GetCommentsRes> getCommentsList(int communityIdx) {
        String query =
                "select c.id as commentIdx,u.id as userIdx,u.nickname, c.content\n" +
                        "from comment c\n" +
                        "inner join user u on c.userIdx = u.id\n" +
                        "where c.boardIdx=? and c.status='A'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetCommentsRes(
                        rs.getInt("commentIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("content")), communityIdx);
    }
}
