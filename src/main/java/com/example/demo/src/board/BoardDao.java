package com.example.demo.src.board;

import com.example.demo.src.board.model.GetExamSubjectListRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
        String query = "select id,title,content,date_format(endAt, '%m/%d') as endAt from exam_sub_board where status='A' and userIdx = ?";
        return this.jdbcTemplate.query(query,
                (rs, rowNum) -> new GetExamSubjectListRes(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("content"),
                        rs.getString("endAt")
                ),userIdx);
    }
}
