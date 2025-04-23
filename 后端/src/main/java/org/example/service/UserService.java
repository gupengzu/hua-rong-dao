package org.example.service;

import org.example.pojo.*;

import java.util.List;

public interface UserService {
    AllUsers query(String username, Integer awardCount, Integer page, Integer pageSize);

    void delete(Integer[] ids);

    void add(User user);

    UserDTO queryById(Integer id);

    List<UserDTO> queryAllUsers();

    LoginInfo login(User user);
}
