package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.pojo.User;
import org.example.pojo.UserDTO;
import org.example.pojo.UserForSpectate;

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

    @Update("update user set award_count=award_count+1 where username=#{userName}")
    void awardAddOne(String userName);

    @Insert("insert into layout(username,count,layout) values(#{username},#{count},#{layout})")
    void storeLayout(String username, Integer count, String layout);

    @Select("select username from layout where username=#{username}")
    String selectByUserNameInLayout(String username);

    @Delete("delete from layout where username=#{username}")
    void deleteByUserNameInLayout(String username);

    @Select("select layout from layout where username=#{username}")
    String getLayout(String username);

    @Select("select count from layout where username=#{username}")
    Integer getCount(String username);

    @Insert("insert into useringame(username,image,layout) values(#{username},#{image},#{layout})")
    void addGameUsers(String username, String image, String layout);

    @Delete("delete from useringame where username=#{username}")
    void deleteGameUsers(String username);

    @Select("select username,image,layout from useringame")
    List<UserForSpectate> queryPlayingUsers();

    @Update("update useringame set layout=#{layout} where username=#{username}")
    void changeGameUsers(String username, String layout);
}
