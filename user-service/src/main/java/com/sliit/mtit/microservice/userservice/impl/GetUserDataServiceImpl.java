package com.sliit.mtit.microservice.userservice.impl;

import com.sliit.mtit.microservice.userservice.dto.UserDetailRequest;
import com.sliit.mtit.microservice.userservice.dto.UserDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class GetUserDataServiceImpl {
    @Autowired
    private RestTemplate getUserDataServiceImplementation;
    public UserDetailResponse getUserData(UserDetailRequest userDetailRequest){
        System.out.println("Order Details" + userDetailRequest);
        var userDetailResponse = new UserDetailResponse();
        userDetailResponse.setUserId("id123");
        userDetailResponse.setUserName("Duminda");
        userDetailResponse.setUserContact("0765742200");
        userDetailResponse.setUserAddress("296-B,Averiwatta Road, Wattala");
        userDetailResponse.setUserEmail("duminda.g.k@gmail.com");
        userDetailResponse.setUserContact("0765742200");
        userDetailResponse.setPassword("12345");
        return userDetailResponse;
    }
    @Bean
    public RestTemplate getUserDataServiceImplementation(RestTemplateBuilder builder){
        return builder.build();
    }
}
