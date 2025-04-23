package org.example.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.example.pojo.AllUsers;
import org.example.pojo.Result;
import org.example.pojo.User;
import org.example.pojo.UserDTO;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

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
}














