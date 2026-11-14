package com.bite.user.controller;


import com.bite.user.api.pojo.UserInfoRequest;
import com.bite.user.api.pojo.UserInfoResponse;
import com.bite.user.api.pojo.UserLoginResponse;
import com.bite.common.pojo.Result;
import com.bite.user.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public Result<UserLoginResponse> login(@Validated @RequestBody UserInfoRequest user){
        log.info("用户登录, userName: {}", user.getUserName());
        return Result.success(userService.login(user));
    }
    @RequestMapping("/getUserInfo")
    public Result<UserInfoResponse> getUserInfo(@NotNull Integer userId){

        return Result.success(userService.getUserInfo(userId));
    }
    @RequestMapping("/getAuthorInfo")
    public Result<UserInfoResponse> getAuthorInfo(@NotNull Integer blogId){
        return Result.success(userService.selectAuthorInfoByBlogId(blogId));
    }
}
