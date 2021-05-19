package com.sliit.mtit.microservice.userservice.impl;

import com.sliit.mtit.microservice.userservice.dto.User;
import com.sliit.mtit.microservice.userservice.dto.UserDetailResponse;
import com.sliit.mtit.microservice.userservice.dto.UserDetailRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserServiceImpl {
    @Autowired
    private RestTemplate restTemplate;

    // Static Data used instead of Database since lectures are not going that deep --------
    public static Map<String, String> messagesArr;
    public static User[] usersObjectArr = new User[2] ;
    static {
        messagesArr = new HashMap<>();
        messagesArr.put("1001","Order created successfully");
        messagesArr.put("1002","User data retrieved successfully");
        messagesArr.put("1003","Product data retrieved successfully");
        messagesArr.put("1004","Successfully Price calculated");
        messagesArr.put("1005","Successfully Shipment data received");
        messagesArr.put("1006","Successfully Discount calculated");

        messagesArr.put("2001","");
        messagesArr.put("2002","User name or password is wrong.try again!");
        messagesArr.put("2003","Unavailable product.try again!");
        messagesArr.put("2004","");
        messagesArr.put("2005","");
        messagesArr.put("2006","");

        usersObjectArr[0] = new User("Duminda","Wattala", "duminda.g.k@gmail.com", "0765742200", "pass123", "type1");
        usersObjectArr[1] = new User("Abinaya","Negombo", "abinaya.yoga@gmail.com", "0765122944", "pass456", "type2");
    }


    // GetSuccessCode Function --------------------------------------------------
    public String GetSuccessCode(String code){
        for (String i : messagesArr.keySet()) {
            if(i.equals(code)){
                return messagesArr.get(i);
            }
        }
        return "Something went wrong";
    }

    // GetUserData API ==================================================================
    public UserDetailResponse GetUserData(UserDetailRequest userDetailRequest){
        UserDetailResponse userDetailResponse = new UserDetailResponse();
        userDetailResponse.setSuccessMessage(GetSuccessCode("2002"));
        userDetailResponse.setSuccessCode("2002");
        for (var user: usersObjectArr) {
            if(user.getUserEmail().equals(userDetailRequest.getUserEmail())){
                if(user.getUserPass().equals(userDetailRequest.getUserPass())){
                    userDetailResponse.setUserShippingAddress(user.getUserShippingAddress());
                    userDetailResponse.setUserContact(user.getUserContact());
                    userDetailResponse.setUserEmail(user.getUserEmail());
                    userDetailResponse.setUserName(user.getUserName());
                    userDetailResponse.setUserPass(user.getUserPass());
                    userDetailResponse.setUserType(user.getUserType());
                    userDetailResponse.setSuccessMessage(GetSuccessCode("1002"));
                    userDetailResponse.setSuccessCode("1002");
                }
                break;
            }
        }
        return userDetailResponse;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
