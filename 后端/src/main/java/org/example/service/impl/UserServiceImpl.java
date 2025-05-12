package org.example.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.UserMapper;
import org.example.pojo.*;
import org.example.service.UserService;
import org.example.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public AllUsers query(String username, Integer awardCount, Integer page, Integer pageSize) {

        PageHelper.startPage(page, pageSize);

        List<UserDTO> userDTOList = userMapper.query(username, awardCount);

        PageInfo<UserDTO>pageInfo=new PageInfo<>(userDTOList);

        long total=pageInfo.getTotal();

        return new AllUsers(total, userDTOList);
    }

    @Override
    public void delete(Integer[] ids) {
        userMapper.delete(ids);
    }

    @Override
    public void add(User user) {
        userMapper.add(user.getUsername(),user.getPassword(),user.getEmail(),user.getAward_count(),user.getImage());
    }

    @Override
    public UserDTO queryById(Integer id) {
        return userMapper.queryById(id);
    }

    @Override
    public List<UserDTO> queryAllUsers() {
        List<UserDTO> userDTOList = userMapper.queryAll();
        return userDTOList;
    }

    @Override
    public LoginInfo login(User user) {
        //1.调用mapper接口，根据用户名和密码查询员工信息
        User u=userMapper.selectByUserNameAndPassword(user);
        //2.判断是否存在员工，如果存在，组装登录成功信息
        if(u!=null){
            log.info("用户登录成功"+u);
            //生成Jwt令牌
            Map<String, Object>claims=new HashMap<>();
            claims.put("id",u.getId());
            claims.put("username",u.getUsername());
            String jwt = JWTUtils.generateToken(claims);

            return new LoginInfo(u.getId(),u.getUsername(),jwt);
        }
        //3.不存在，返回null
        return null;
    }

    @Override
    public void changeImage(String username, String url) {
        userMapper.changeImage(username,url);
    }

    @Override
    public void awardAddOne(String userName) {
        userMapper.awardAddOne(userName);
    }

    @Override
    public void storeLayout(StoreLayout store) {
        if(userMapper.selectByUserNameInLayout(store.getUsername())==null){
            userMapper.storeLayout(store.getUsername(),store.getCount(),store.getLayout());
        }else{
            userMapper.deleteByUserNameInLayout(store.getUsername());
            userMapper.storeLayout(store.getUsername(),store.getCount(),store.getLayout());
        }

    }

    @Override
    public String getLayout(String username) {
        return userMapper.getLayout(username);
    }

    @Override
    public Integer getCount(String username) {
        return userMapper.getCount(username);
    }

    @Override
    public void addGameUsers(UserForSpectate user) {
        userMapper.addGameUsers(user.getUsername(),user.getImage(),user.getLayout());
    }

    @Override
    public void deleteGameUsers(UserForSpectate user) {
        userMapper.deleteGameUsers(user.getUsername());
    }

    @Override
    public List<UserForSpectate> queryPlayingUsers() {
        List<UserForSpectate> userForSpectateList = userMapper.queryPlayingUsers();
        return userForSpectateList;
    }

    @Override
    public void changeGameUsers(UserForSpectate user) {
        userMapper.changeGameUsers(user.getUsername(),user.getLayout());
    }

}
