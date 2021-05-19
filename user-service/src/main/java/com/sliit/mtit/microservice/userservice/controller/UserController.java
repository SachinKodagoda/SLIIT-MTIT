package com.sliit.mtit.microservice.userservice.controller;

import com.sliit.mtit.microservice.userservice.dto.UserDetailRequest;
import com.sliit.mtit.microservice.userservice.dto.UserDetailResponse;
import com.sliit.mtit.microservice.userservice.impl.GetUserDataServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private GetUserDataServiceImpl getUserDataService;

    @PostMapping(consumes = "application/json", produces="application/json")
    public @ResponseBody UserDetailResponse getUser(@RequestBody UserDetailRequest userDetailRequest){
        return getUserDataService.getUserData(userDetailRequest);
    }
}
