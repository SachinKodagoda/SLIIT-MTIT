package com.sliit.mtit.microservice.userservice.controller;

import com.sliit.mtit.microservice.userservice.dto.UserDetailResponse;
import com.sliit.mtit.microservice.userservice.dto.UserDetailRequest;
import com.sliit.mtit.microservice.userservice.impl.UserServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @PostMapping(consumes = "application/json", produces="application/json")
    public @ResponseBody UserDetailResponse UserController(@RequestBody UserDetailRequest request){
        return userServiceImpl.GetUserData(request);
    }
}