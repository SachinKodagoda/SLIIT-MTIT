package com.sliit.mtit.microservice.discountservice.impl;

import com.sliit.mtit.microservice.discountservice.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DiscountServiceImpl {
    @Autowired
    private RestTemplate restTemplate;

    // Static Data used instead of Database since lectures are not going that deep --------
    public static Map<String, String> messagesArr;
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


    // CalculateUserDiscount API ==================================================================
    public UserDiscountResponse UserDiscount(UserDiscountRequest userDiscountRequest){
        UserDiscountResponse userDiscountResponse = new UserDiscountResponse();
        String userType = "type4";
        double discount;
        // Validate User ....
        UserDetailRequest userDetailRequest = new UserDetailRequest();
        userDetailRequest.setUserEmail(userDiscountRequest.getUserEmail());
        userDetailRequest.setUserPass(userDiscountRequest.getUserPass());

        ResponseEntity<UserDetailResponse> responseEntity1 = restTemplate.postForEntity("http://localhost:8085/users", userDetailRequest, UserDetailResponse.class);
        UserDetailResponse userDetailResponse =  responseEntity1.getBody();
        if(userDetailResponse.getSuccessCode().equals("1002") ){
            if(userDetailResponse.getUserType() != null){
                userType = userDetailResponse.getUserType();
            }
            switch(userType) {
                case "type1":
                    discount = 0.05;
                    break;
                case "type2":
                    discount = 0.02;
                    break;
                case "type3":
                    discount = 0.01;
                    break;
                case "type4":
                default:
                    discount = 0;
            }
            userDiscountResponse.setUserEmail(userDetailResponse.getUserEmail());
            userDiscountResponse.setUserType(userType);
            userDiscountResponse.setDiscountPercentage(discount);
            userDiscountResponse.setSuccessMessage(GetSuccessCode("1006"));
            userDiscountResponse.setSuccessCode("1006");
        }else{
            System.out.println("UserDiscount - UserEmail or Password Error");
            userDiscountResponse.setSuccessMessage(GetSuccessCode(userDetailResponse.getSuccessCode()));
            userDiscountResponse.setSuccessCode(userDetailResponse.getSuccessCode());
        }
        return userDiscountResponse;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
