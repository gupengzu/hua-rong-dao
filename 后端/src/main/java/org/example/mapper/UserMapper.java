package org.example.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.User;
import org.example.pojo.UserDTO;

import java.util.List;

@Mapper
public interface UserMapper {

    List<UserDTO> query(String username, Integer awardCount);

    void delete(Integer[] ids);

    @Insert("insert into user(username,password,email,award_count,image) values(#{username},#{password},#{email},#{award_count},#{image})")
    void add(String username, String password,String email, Integer award_count, String image);

    @Select("select * from user where id=#{id}")
    UserDTO queryById(Integer id);

    @Select("select * from user order by award_count DESC ")
    List<UserDTO> queryAll();

    @Select("select id,username from user where username=#{username} and password=#{password}")
    User selectByUserNameAndPassword(User user);

    @Insert("update user set image=#{url} where username=#{username}")
    void changeImage(String username, String url);
}
