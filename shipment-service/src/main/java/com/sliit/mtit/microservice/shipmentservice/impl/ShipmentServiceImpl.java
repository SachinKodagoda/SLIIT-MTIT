package com.sliit.mtit.microservice.shipmentservice.impl;

import com.sliit.mtit.microservice.shipmentservice.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ShipmentServiceImpl {
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

    // GetShipmentData API ==================================================================
    public String GetCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        return sdf.format(cal.getTime());
    }
    public ShipmentResponse GetShipmentData(ShipmentRequest shipmentRequest){
        ShipmentResponse shipmentResponse = new ShipmentResponse();
        // Validate User and Get Data....
        UserDetailRequest userDetailRequest =  new UserDetailRequest();
        userDetailRequest.setUserEmail(shipmentRequest.getUserEmail());
        userDetailRequest.setUserPass(shipmentRequest.getUserPass());
        ResponseEntity<UserDetailResponse> responseEntity = restTemplate.postForEntity("http://localhost:8085/users", userDetailRequest, UserDetailResponse.class);
        UserDetailResponse userDetailResponse = responseEntity.getBody();
        if(userDetailResponse.getSuccessCode().equals("1002")){
            // Set Shipment data
            shipmentResponse.setShippingAddress(userDetailResponse.getUserShippingAddress());
            shipmentResponse.setPossibleShippingDate(GetCurrentDate());
            shipmentResponse.setShippingTrackId(UUID.randomUUID().toString());
            shipmentResponse.setSuccessMessage(GetSuccessCode("1005"));
            shipmentResponse.setSuccessCode("1005");
        }else{
            System.out.println("GetShipmentData - Shipment Error");
            shipmentResponse.setSuccessMessage(GetSuccessCode(userDetailResponse.getSuccessCode()));
            shipmentResponse.setSuccessCode(userDetailResponse.getSuccessCode());
        }
        return shipmentResponse;
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
