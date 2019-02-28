package com.second.practiceproject2.mapper;

import com.second.practiceproject2.model.Question;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

//@Mapper
//@Component
//@Repository
public interface QuestionMapper {
    String TABLE_NAME = "question";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = "id, "+ INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, " (",INSERT_FIELDS,
            ") values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    public int addQuestion(Question question);

    //下面对应QuestionMapper.xml
    List<Question> selectLatestQuestions(@Param("userId") int userId,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);


}
