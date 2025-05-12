package org.example.service;

import org.example.pojo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    AllUsers query(String username, Integer awardCount, Integer page, Integer pageSize);

    void delete(Integer[] ids);

    void add(User user);

    UserDTO queryById(Integer id);

    List<UserDTO> queryAllUsers();

    LoginInfo login(User user);

    void changeImage(String username, String url);

    void awardAddOne(String userName);

    void storeLayout(StoreLayout store);

    String getLayout(String username);

    Integer getCount(String username);

    void addGameUsers(UserForSpectate user);

    void deleteGameUsers(UserForSpectate user);

    List<UserForSpectate> queryPlayingUsers();

    void changeGameUsers(UserForSpectate user);
}
