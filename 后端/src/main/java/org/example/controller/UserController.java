package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.example.pojo.*;
import org.example.service.UserService;
import org.example.utils.AliyunOSSOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @GetMapping
    public Result query(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "award_count", required = false) Integer awardCount,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "pageSize", required = true) Integer pageSize) {

        AllUsers allUsers= userService.query(username, awardCount, page, pageSize);
        return Result.success(allUsers);
    }

    @DeleteMapping
    public Result delete(@RequestParam("ids")Integer[]ids){
        userService.delete(ids);
        return Result.success();
    }

    @PostMapping
    public Result add(@RequestBody User user){
        userService.add(user);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result queryById(@PathVariable Integer id){
        UserDTO userDTO= userService.queryById(id);
        return Result.success(userDTO);
    }

    @GetMapping("/list")
    public Result queryAll(){
        List<UserDTO> allUsers=userService.queryAllUsers();
        return Result.success(allUsers);
    }

    @PostMapping("/image")
    public Result changeImage(@RequestBody For_image forImage) throws Exception {
        String username=forImage.getUsername();
        String url=forImage.getUrl();
        userService.changeImage(username,url);
        return Result.success();
    }
}














