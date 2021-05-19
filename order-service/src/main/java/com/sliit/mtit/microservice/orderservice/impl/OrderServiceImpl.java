package com.sliit.mtit.microservice.orderservice.impl;

import com.sliit.mtit.microservice.orderservice.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class OrderServiceImpl {
    @Autowired
    private RestTemplate restTemplate;

    // Static Data used instead of Database since lectures are not going that deep --------
    public static Map<String, String> messagesArr;
    public static Map<String, String> productArr;
    public static User[] usersObjectArr = new User[2] ;
    static {
        messagesArr = new HashMap<>();
        productArr = new HashMap<>();
        messagesArr.put("1001","Order created successfully");
        messagesArr.put("2001","Invalid User");
        productArr.put("pr001","20");
        productArr.put("pr002","40");
        productArr.put("pr003","60");
        usersObjectArr[0] = new User("Duminda","Dankotuwa", "duminda.g.k@gmail.com", "0765742200", "pass123", "type1");
        usersObjectArr[1] = new User("Abinaya","Negombo", "abinaya.yoga@gmail.com", "0765122944", "pass456", "type2");
    }

    // CheckValidUser --------------------------------------------------
    public boolean CheckValidUser(OrderRequest orderRequest){
        for (String i : messagesArr.keySet()) {
            if(i.equals(orderRequest.getUserEmail())){
                return true;
            }
        }
        return false;
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

    // GetSuccessCode Function --------------------------------------------------
    public double TaxCalculation(double billAmount){
        if (billAmount > 2000) return billAmount * 0.01;
        return 0;
    }

    // Calculate Current Date Function --------------------------------------------------
    public String GetCurrentDate(){
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    // GetUserData API ==================================================================
    public UserDetailResponse GetUserData(UserDetailRequest userDetailRequest){
        UserDetailResponse userDetailResponse = new UserDetailResponse();
        for (var user: usersObjectArr) {
            if(user.getUserEmail().equals(userDetailRequest.getUserEmail())){
                userDetailResponse.setUserShippingAddress(user.getUserShippingAddress());
                userDetailResponse.setUserContact(user.getUserContact());
                userDetailResponse.setUserEmail(user.getUserEmail());
                userDetailResponse.setUserName(user.getUserName());
                userDetailResponse.setPassword(user.getPassword());
                break;
            }
        }
        return userDetailResponse;
    }


    // GetShipmentData API ==================================================================
    public ShipmentResponse GetShipmentData(ShipmentRequest shipmentRequest){
        ShipmentResponse shipmentResponse = new ShipmentResponse();
        for (var user: usersObjectArr) {
            if(user.getUserEmail().equals(shipmentRequest.getUserEmail())){
                shipmentResponse.setShippingAddress(user.getUserShippingAddress());
                break;
            }
        }

        shipmentResponse.setPossibleShippingDate(GetCurrentDate());
        shipmentResponse.setShippingTrackId(UUID.randomUUID().toString());
        return shipmentResponse;
    }



    // CalculatePrice API ==================================================================
    public PriceCalculationResponse CalculatePrice(PriceCalculationRequest priceCalculationRequest){
        UserDiscountRequest userDiscountRequest = new UserDiscountRequest();
        userDiscountRequest.setUserEmail(priceCalculationRequest.getUserEmail());
        UserDiscountResponse userDiscountResponse = CalculateUserDiscount(userDiscountRequest);
        double subTotal = priceCalculationRequest.getQuantity() * 5000;
        double tax = TaxCalculation(subTotal);
        double margin = 1000.0;
        double totalDiscount = 0;
        if(subTotal > margin){
            totalDiscount = (subTotal - margin) * userDiscountResponse.getDiscountPercentage();
        }
        PriceCalculationResponse priceCalculationResponse = new PriceCalculationResponse();
        priceCalculationResponse.setSubTotal(subTotal);
        priceCalculationResponse.setTax(tax);
        priceCalculationResponse.setTotalDiscount(totalDiscount);
        priceCalculationResponse.setTotal(subTotal-totalDiscount-tax);
        return priceCalculationResponse;
    }




    // CreateOrder API ==================================================================
    public OrderResponse CreateOrder(OrderRequest orderRequest){
        // This is the order response object
        OrderResponse orderResponse = new OrderResponse();

        // Check User availability and set Data accordingly
        boolean validUser = CheckValidUser(orderRequest);
        String successCode = validUser ? "1001": "2001";
        orderResponse.setSuccessMessage(GetSuccessCode(successCode));
        orderResponse.setSuccessCode(successCode);

        if(validUser){
            // Get shipping data
            ShipmentRequest shipmentRequest = new ShipmentRequest();
            shipmentRequest.setUserEmail(orderRequest.getUserEmail());
            ShipmentResponse shipmentResponse = GetShipmentData(shipmentRequest);
            orderResponse.setShippingTrackId(shipmentResponse.getShippingTrackId());
            orderResponse.setShippingAddress(shipmentResponse.getShippingAddress());
            orderResponse.setPossibleShippingDate(shipmentResponse.getPossibleShippingDate());

            // Calculate price
            PriceCalculationRequest priceCalculationRequest = new PriceCalculationRequest();
            priceCalculationRequest.setProductId(orderRequest.getProductId());
            priceCalculationRequest.setQuantity(orderRequest.getQuantity());
            priceCalculationRequest.setUserEmail(orderRequest.getUserEmail());
            PriceCalculationResponse priceCalculationResponse = CalculateOrder(priceCalculationRequest);
            orderResponse.setTax(priceCalculationResponse.getTax());
            orderResponse.setSubTotal(priceCalculationResponse.getSubTotal());
            orderResponse.setTotal(priceCalculationResponse.getTotal());
            orderResponse.setTotalDiscount(priceCalculationResponse.getTotalDiscount());
        }

        // send order ID as a response
        return orderResponse;
    }

    // CalculateUserDiscount API ==================================================================
    public UserDiscountResponse CalculateUserDiscount(UserDiscountRequest userDiscountRequest){
        String userType = "type4";
        double discount;
        UserDiscountResponse userDiscountResponse = new UserDiscountResponse();
        for (var user: usersObjectArr) {
            if(user.getUserEmail().equals(userDiscountRequest.getUserEmail())){
                userType =  user.getUserType();
            }
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
        userDiscountResponse.setUserEmail(userDiscountRequest.getUserEmail());
        userDiscountResponse.setUserType(userType);
        userDiscountResponse.setDiscountPercentage(discount);
        return userDiscountResponse;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
