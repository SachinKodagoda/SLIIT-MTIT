package com.sliit.mtit.microservice.orderservice.impl;

import com.sliit.mtit.microservice.orderservice.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderServiceImpl {
    @Autowired
    private RestTemplate restTemplate;

    // Static Data used instead of Database since lectures are not going that deep --------
    public static Map<String, String> messagesArr;
    public static User[] usersObjectArr = new User[2] ;
    public static Product[] productsObjectArr = new Product[3] ;
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

        productsObjectArr[0] = new Product("pr1001","Biscuit", 20.0, "Maliban");
        productsObjectArr[1] = new Product("pr1002","Milk", 40.0, "Anchor");
        productsObjectArr[2] = new Product("pr1003","Ice Cream", 30.0, "Haagen-Dazs");
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



    // CreateOrder API ==================================================================
    public OrderResponse CreateOrder(OrderRequest orderRequest){
        // This is the order response object
        OrderResponse orderResponse = new OrderResponse();
        // Validate User ....
        UserDetailRequest userDetailRequest = new UserDetailRequest();
        userDetailRequest.setUserEmail(orderRequest.getUserEmail());
        userDetailRequest.setUserPass(orderRequest.getUserPass());
        ResponseEntity<UserDetailResponse> responseEntity1 = restTemplate.postForEntity("http://localhost:8085/users", userDetailRequest, UserDetailResponse.class);
        UserDetailResponse userDetailResponse =  responseEntity1.getBody();
        if(userDetailResponse.getSuccessCode().equals("1002") ){
            // Get Shipping Data ....
            ShipmentRequest shipmentRequest = new ShipmentRequest();
            shipmentRequest.setUserEmail(orderRequest.getUserEmail());
            shipmentRequest.setUserPass(orderRequest.getUserPass());
            ResponseEntity<ShipmentResponse> responseEntity2 = restTemplate.postForEntity("http://localhost:8084/shipment", shipmentRequest, ShipmentResponse.class);
            ShipmentResponse shipmentResponse =  responseEntity2.getBody();
            if(shipmentResponse.getSuccessCode().equals("1005")){
                // Get Product Details ....
                ProductRequest productRequest = new ProductRequest();
                productRequest.setProductId(orderRequest.getProductId());
                ResponseEntity<ProductResponse> responseEntity3 = restTemplate.postForEntity("http://localhost:8083/products", productRequest, ProductResponse.class);
                ProductResponse productResponse =  responseEntity3.getBody();
                if(productResponse.getSuccessCode().equals("1003")){
                    // Get Calculated price ....
                    PriceCalculationRequest priceCalculationRequest = new PriceCalculationRequest();
                    priceCalculationRequest.setProductId(orderRequest.getProductId());
                    priceCalculationRequest.setProductQuantity(orderRequest.getProductQuantity());
                    priceCalculationRequest.setUserEmail(orderRequest.getUserEmail());
                    priceCalculationRequest.setUserPass(orderRequest.getUserPass());
                    ResponseEntity<PriceCalculationResponse> responseEntity4 = restTemplate.postForEntity("http://localhost:8082/price", priceCalculationRequest, PriceCalculationResponse.class);
                    PriceCalculationResponse priceCalculationResponse =  responseEntity4.getBody();
                    if(priceCalculationResponse.getSuccessCode().equals("1004")){
                        // Request is totally successful
                        // Set Shipping Data
                        orderResponse.setShippingTrackId(shipmentResponse.getShippingTrackId());
                        orderResponse.setShippingAddress(shipmentResponse.getShippingAddress());
                        orderResponse.setPossibleShippingDate(shipmentResponse.getPossibleShippingDate());
                        // Set Product Data
                        orderResponse.setProductQuantity(orderRequest.getProductQuantity());
                        orderResponse.setProductName(productResponse.getProductName());
                        orderResponse.setProductId(productResponse.getProductId());
                        orderResponse.setProductSeller(productResponse.getProductSeller());
                        orderResponse.setProductPrice(productResponse.getProductPrice());
                        // Set Calculated price
                        orderResponse.setTax(priceCalculationResponse.getTax());
                        orderResponse.setSubTotal(priceCalculationResponse.getSubTotal());
                        orderResponse.setTotal(priceCalculationResponse.getTotal());
                        orderResponse.setTotalDiscount(priceCalculationResponse.getTotalDiscount());
                        // Set Success Message and Order ID
                        orderResponse.setOrderId(UUID.randomUUID().toString());
                        orderResponse.setSuccessMessage(GetSuccessCode("1001"));
                        orderResponse.setSuccessCode("1001");
                    }else{
                        System.out.println("CreateOrder - Price Calculation Error");
                        orderResponse.setSuccessMessage(GetSuccessCode(priceCalculationResponse.getSuccessCode()));
                        orderResponse.setSuccessCode(priceCalculationResponse.getSuccessCode());
                    }
                }else{
                    System.out.println("CreateOrder - Product Error");
                    orderResponse.setSuccessMessage(GetSuccessCode(productResponse.getSuccessCode()));
                    orderResponse.setSuccessCode(productResponse.getSuccessCode());
                }
            }else{
                System.out.println("CreateOrder - Shipment Error");
                orderResponse.setSuccessMessage(GetSuccessCode(shipmentResponse.getSuccessCode()));
                orderResponse.setSuccessCode(shipmentResponse.getSuccessCode());
            }
        }else{
            System.out.println("CreateOrder - UserEmail or Password Error");
            orderResponse.setSuccessMessage(GetSuccessCode(userDetailResponse.getSuccessCode()));
            orderResponse.setSuccessCode(userDetailResponse.getSuccessCode());
        }
        return orderResponse;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
