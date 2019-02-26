package com.second.practiceproject2.dao;

import com.second.practiceproject2.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionDAO {
    String TABLE_NAME = "question";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = "id, "+ INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, " (",INSERT_FIELDS,
            ") values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    //下面对应xml
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);


}
