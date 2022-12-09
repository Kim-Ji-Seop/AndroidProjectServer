package com.example.demo.src.board;

import com.example.demo.config.BaseException;
import com.example.demo.src.board.model.*;
import jdk.internal.org.jline.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

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
                "select esb.id,esb.subjectName,esb.professor,esb.separation,esb.grade,esb.time,esb.room,esb.credit,IFNULL((select round(avg(sr.score),1) as score\n" +
                        "                                                                                                    from sub_review sr\n" +
                        "                                                                                                    where esb.id = sr.subjectID),0) as scoreAverage\n" +
                        "from evaluate_sub_board esb\n" +
                        "where esb.status='A'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetEvaluateSubjectRes(
                        rs.getInt("id"),
                        rs.getString("subjectName"),
                        rs.getString("professor"),
                        rs.getString("separation"),
                        rs.getInt("grade"),
                        rs.getString("time"),
                        rs.getString("room"),
                        rs.getInt("credit"),
                        rs.getFloat("scoreAverage")
                )
        );

    }

    // 학년별 과목 조회
    public List<GetEvaluateSubjectRes> getEvaluateSubjectGradeList(Integer grade) {
        String query =
                "select esb.id,esb.subjectName,esb.professor,esb.separation,esb.grade,esb.time,esb.room,esb.credit,IFNULL((select round(avg(sr.score),1) as score\n" +
                        "                                                                                                    from sub_review sr\n" +
                        "                                                                                                    where esb.id = sr.subjectID),0) as scoreAverage\n" +
                        "from evaluate_sub_board esb\n" +
                        "where esb.status='A' and grade=?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetEvaluateSubjectRes(
                        rs.getInt("id"),
                        rs.getString("subjectName"),
                        rs.getString("professor"),
                        rs.getString("separation"),
                        rs.getInt("grade"),
                        rs.getString("time"),
                        rs.getString("room"),
                        rs.getInt("credit"),
                        rs.getFloat("scoreAverage")
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
                "select u.nickname,sr.content,sr.score\n" +
                        "from sub_review sr\n" +
                        "inner join evaluate_sub_board esb on sr.subjectID = esb.id\n" +
                        "inner join user u on sr.userIdx = u.id\n" +
                        "where esb.id = ?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetEvaluateSubjectReviewRes(
                        rs.getString("nickName"),
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
                "select cb.id,cb.userIdx,u.grade,title,content,case\n" +
                        "                                                when (timestampdiff(second ,cb.createdAt,now()) between 1 and 59) then concat(cast(timestampdiff(second ,cb.createdAt,now()) as char),'초 전')\n" +
                        "                                                when (timestampdiff(minute ,cb.createdAt,now()) between 1 and 59) then concat(cast(timestampdiff(minute ,cb.createdAt,now()) as char),'분 전')\n" +
                        "                                                when (timestampdiff(hour ,cb.createdAt,now()) between 1 and 24) then concat(cast(timestampdiff(hour ,cb.createdAt,now()) as char),'시간 전')\n" +
                        "                                                when (datediff(now(),cb.createdAt) between 1 and 30) then concat(cast(datediff(now(),cb.createdAt) as char), '일 전')\n" +
                        "                                              end as createdAt,\n" +
                        "    (select count(c.id) from comment c where c.boardIdx = cb.id) as commentCount,\n" +
                        "    date_format(cb.createdAt,'%m/%d %H:%i') as correctCreatedAt,u.nickname\n" +
                        "from community_board cb\n" +
                        "inner join user u on cb.userIdx = u.id\n" +
                        "where cb.status='A'\n" +
                        "order by cb.createdAt desc";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetCommunitiesRes(
                        rs.getInt("id"),
                        rs.getInt("userIdx"),
                        rs.getInt("grade"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("createdAt"),
                        rs.getInt("commentCount"),
                        rs.getString("correctCreatedAt"),
                        rs.getString("nickname"))
        );

    }

    //
    public List<GetCommunitiesRes> getCommunitiesListGradeList(Integer grade) {
        String query =
                "select cb.id,cb.userIdx,u.grade,title,content,case\n" +
                        "                                                when (timestampdiff(second ,cb.createdAt,now()) between 1 and 59) then concat(cast(timestampdiff(second ,cb.createdAt,now()) as char),'초 전')\n" +
                        "                                                when (timestampdiff(minute ,cb.createdAt,now()) between 1 and 59) then concat(cast(timestampdiff(minute ,cb.createdAt,now()) as char),'분 전')\n" +
                        "                                                when (timestampdiff(hour ,cb.createdAt,now()) between 1 and 24) then concat(cast(timestampdiff(hour ,cb.createdAt,now()) as char),'시간 전')\n" +
                        "                                                when (datediff(now(),cb.createdAt) between 1 and 30) then concat(cast(datediff(now(),cb.createdAt) as char), '일 전')\n" +
                        "                                              end as createdAt,\n" +
                        "    (select count(c.id) from comment c where c.boardIdx = cb.id) as commentCount,\n" +
                        "    date_format(cb.createdAt,'%m/%d %H:%i') as correctCreatedAt,u.nickname\n" +
                        "from community_board cb\n" +
                        "inner join user u on cb.userIdx = u.id\n" +
                        "where cb.status='A' and u.grade=?\n" +
                        "order by cb.createdAt desc";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetCommunitiesRes(
                        rs.getInt("id"),
                        rs.getInt("userIdx"),
                        rs.getInt("grade"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("createdAt"),
                        rs.getInt("commentCount"),
                        rs.getString("correctCreatedAt"),
                        rs.getString("nickname")), grade);
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
                "select c.id as commentIdx,u.id as userIdx,u.nickname, c.content,date_format(c.createdAt,'%m/%d %H:%i') as correctCreatedAt\n" +
                        "from comment c\n" +
                        "inner join user u on c.userIdx = u.id\n" +
                        "where c.boardIdx=? and c.status='A'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetCommentsRes(
                        rs.getInt("commentIdx"),
                        rs.getInt("userIdx"),
                        rs.getString("nickname"),
                        rs.getString("content"),
                        rs.getString("correctCreatedAt")), communityIdx);
    }

    public List<GetCoursesRes> getCoursesListAllList() {
        String query =
                "select esb.id,esb.grade,esb.subjectName,esb.professor,esb.`time`,esb.room,esb.separation,esb.credit,IFNULL((select round(avg(sr.score),1) as score\n" +
                        "                                                                                                    from sub_review sr\n" +
                        "                                                                                                    where esb.id = sr.subjectID),0) as scoreAverage\n" +
                        "from evaluate_sub_board esb";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetCoursesRes(
                        rs.getInt("id"),
                        rs.getInt("grade"),
                        rs.getString("subjectName"),
                        rs.getString("professor"),
                        rs.getString("time"),
                        rs.getString("room"),
                        rs.getString("separation"),
                        rs.getInt("credit"),
                        rs.getFloat("scoreAverage"))
        );
    }

    public List<GetCoursesRes> getCoursesListGradeList(Integer grade) {
        String query =
                "select esb.id,esb.grade,esb.subjectName,esb.professor,esb.`time`,esb.room,esb.separation,esb.credit,IFNULL((select round(avg(sr.score),1) as score\n" +
                        "                                                                                                    from sub_review sr\n" +
                        "                                                                                                    where esb.id = sr.subjectID),0) as scoreAverage\n" +
                        "from evaluate_sub_board esb\n" +
                        "where esb.grade=?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetCoursesRes(
                        rs.getInt("id"),
                        rs.getInt("grade"),
                        rs.getString("subjectName"),
                        rs.getString("professor"),
                        rs.getString("time"),
                        rs.getString("room"),
                        rs.getString("separation"),
                        rs.getInt("credit"),
                        rs.getFloat("scoreAverage")),grade
        );
    }

    @Transactional(rollbackFor = {Exception.class})
    public PostCourseRes createCourse(int userIdx, int courseIdx) throws BaseException {
        if(createFlag(userIdx,courseIdx) == 1){
            throw new BaseException(POST_COURSE_EXISTS); // 2024 : 이미 추가한 강의입니다.
        }
        String query = "insert into user_map_course (userIdx,courseIdx) values (?,?)";
        Object[] params = new Object[]{
                userIdx, courseIdx
        };
        this.jdbcTemplate.update(query, params);

        String lastInsertIdQuery = "select last_insert_id()";

        int lastInsertId = this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);

        String selectQuery = "select courseIdx \n" +
                "from user_map_course \n" +
                "where id = ?";
        return this.jdbcTemplate.queryForObject(selectQuery,
                (rs,rowNum)-> new PostCourseRes(
                        rs.getInt("courseIdx")
                ),lastInsertId);
    }
    public int createFlag(int userIdx, int courseIdx){

        String query =
                "select exists(select id\n" +
                        "      from user_map_course\n" +
                        "      where userIdx=? and courseIdx=? and status='A')";
        return this.jdbcTemplate.queryForObject(query,
                int.class,
                userIdx,courseIdx);
    }

    public List<GetTimeTableRes> getTimeTableList(int userIdx) {
        String query =
                "select umc.id,esb.grade,esb.subjectName,esb.professor,esb.time,esb.room,esb.separation,esb.credit\n" +
                        "from evaluate_sub_board esb\n" +
                        "join user_map_course umc on esb.id = umc.courseIdx\n" +
                        "where umc.userIdx=? and umc.status='A'";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetTimeTableRes(
                        rs.getInt("id"),
                        rs.getInt("grade"),
                        rs.getString("subjectName"),
                        rs.getString("professor"),
                        rs.getString("time"),
                        rs.getString("room"),
                        rs.getString("separation"),
                        rs.getInt("credit")),userIdx
        );
    }
    @Transactional(rollbackFor = {Exception.class})
    public DeleteCourseRes deleteMyCourse(int userIdx, int timetableIdx) {
        String query = "update user_map_course set status='D' where id=? and userIdx=?";
        Object[] params = new Object[]{
                timetableIdx,
                userIdx
        };
        this.jdbcTemplate.update(query, params);

        // 방금 수정한 정보 조회
        String responseQuery = "select id,status from user_map_course where id=? and userIdx=?";
        return this.jdbcTemplate.queryForObject(responseQuery,
                (rs, rowNum) -> new DeleteCourseRes(
                        rs.getInt("id"),
                        rs.getString("status")),
                timetableIdx,userIdx);
    }

    public List<GetTopReviewRes> getTopReviews() {
        String query =
                "select esb.subjectName,esb.professor,sr.content,sr.score\n" +
                        "from sub_review sr\n" +
                        "join evaluate_sub_board esb on esb.id = sr.subjectID\n" +
                        "where sr.status='A'\n" +
                        "order by sr.createdAt desc limit 4";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetTopReviewRes(
                        rs.getString("subjectName"),
                        rs.getString("professor"),
                        rs.getString("content"),
                        rs.getFloat("score"))
        );
    }

    public List<GetTopCommunitiesRes> getTopCommunities() {
        String query =
                "select cb.id,cb.userIdx,u.grade,title,content,case\n" +
                        "                                                when (timestampdiff(second ,cb.createdAt,now()) between 1 and 59) then concat(cast(timestampdiff(second ,cb.createdAt,now()) as char),'초 전')\n" +
                        "                                                when (timestampdiff(minute ,cb.createdAt,now()) between 1 and 59) then concat(cast(timestampdiff(minute ,cb.createdAt,now()) as char),'분 전')\n" +
                        "                                                when (timestampdiff(hour ,cb.createdAt,now()) between 1 and 24) then concat(cast(timestampdiff(hour ,cb.createdAt,now()) as char),'시간 전')\n" +
                        "                                                when (datediff(now(),cb.createdAt) between 1 and 30) then concat(cast(datediff(now(),cb.createdAt) as char), '일 전')\n" +
                        "                                              end as createdAt,\n" +
                        "    (select count(c.id) from comment c where c.boardIdx = cb.id) as commentCount\n" +
                        "from community_board cb\n" +
                        "inner join user u on cb.userIdx = u.id\n" +
                        "where cb.status='A'\n" +
                        "order by (select count(c.id) from comment c where c.boardIdx = cb.id) desc limit 4";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetTopCommunitiesRes(
                        rs.getInt("id"),
                        rs.getInt("userIdx"),
                        rs.getInt("grade"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("createdAt"),
                        rs.getInt("commentCount"))
        );
    }

    public List<GetRemainTimeExamSubjectRes> getRemainTimes(int userIdx) {
        String query =
                "select esb.id,esb.title, esb.content, date_format(esb.endAt,'%Y-%m-%d') as endAt\n" +
                        "from exam_sub_board esb\n" +
                        "where current_date <= date(esb.endAt) and status='A' and esb.userIdx=?\n" +
                        "order by esb.endAt";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetRemainTimeExamSubjectRes(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("endAt")),userIdx
        );
    }
}
