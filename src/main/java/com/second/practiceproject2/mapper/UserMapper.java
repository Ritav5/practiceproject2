package com.second.practiceproject2.mapper;

import com.second.practiceproject2.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

//@Mapper
//@Component
//@Repository
public interface UserMapper {
    String TABLE_NAME = "user";
    String INSERT_FIELDS = " name,password,salt,head_url ";
    String SELECT_FIELDS = "id, "+ INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, " (",INSERT_FIELDS,
            ") values(#{name},#{password},#{salt},#{headUrl})"})
    public int addUser(User user);

    @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME,"where id = #{id}"})
    public User selectById(int id);

    @Select({"select ", SELECT_FIELDS, "from ", TABLE_NAME,"where name = #{name}"})
    public User selectByName(String name);

    @Update({"update", TABLE_NAME,"set password = #{password} where id = #{id}"})
    public void updatePassword(User user);

    @Delete({"delete form", TABLE_NAME,"where id = #{id}"})
    public void deleteById(int id);

}
