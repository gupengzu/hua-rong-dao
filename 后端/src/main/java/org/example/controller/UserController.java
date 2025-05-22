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
    @PostMapping("/award")
    public Result awardAddOne(@RequestBody User user){
        String userName=user.getUsername();
        userService.awardAddOne(userName);
        return Result.success();
    }

    @PostMapping("/storeLayout")
    public Result storeLayout(@RequestBody StoreLayout store){
        userService.storeLayout(store);
        return Result.success();
    }
    @PostMapping("/getLayout")
    public Result getLayout(@RequestBody StoreLayout store){
        String username=store.getUsername();
        Boolean userExist=(userService.getLayout(username)!=null);
        //用户根本没有存过档
        if(!userExist){
            return Result.success();
        } else if (!isVaildLayout(userService.getLayout(username))) {
            //layout是个字符串，保证其仅由0-5的数字组成，且有且仅有一个5
            return Result.success();
        } else {
            store.setLayout(userService.getLayout(username));
            store.setCount(userService.getCount(username));
            return Result.success(store);
        }
    }
    public boolean isVaildLayout(String layout){
        //layout是个字符串，保证其长度为20，保证其仅由0-5的数字组成，且有且仅有一个5
        if (layout.length() != 20) {
            return false;
        }
        int count = 0;
        for (int i = 0; i < layout.length(); i++) {
            char c = layout.charAt(i);
            if (c < '0' || c > '5') {
                return false;
            }
            if (c == '5') {
                count++;
            }
        }
        return count == 1;
    }



    @PostMapping("/addPlayingUser")
    public Result addGameUsers(@RequestBody UserForSpectate user){
        userService.addGameUsers(user);
        return Result.success();
    }

    @PostMapping("/changePlayingUser")
    public Result changeGameUsers(@RequestBody UserForSpectate user){
        userService.changeGameUsers(user);
        return Result.success();
    }

    @PostMapping("/deletePlayingUser")
    public Result deleteGameUsers(@RequestBody UserForSpectate user){
        userService.deleteGameUsers(user);
        return Result.success();
    }

    @GetMapping("/getPlayingUsers")
    public Result playingUsers(){
        List<UserForSpectate> allUsers=userService.queryPlayingUsers();
        return Result.success(allUsers);
    }
}














