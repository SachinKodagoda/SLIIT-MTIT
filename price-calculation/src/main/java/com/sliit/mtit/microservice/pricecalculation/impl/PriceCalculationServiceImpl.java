package com.sliit.mtit.microservice.pricecalculation.impl;

import com.sliit.mtit.microservice.pricecalculation.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PriceCalculationServiceImpl {
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

    // CalculatePrice API ==================================================================
    public double TaxCalculation(double billAmount){
        if (billAmount > 2000) return billAmount * 0.01;
        return 0;
    }

    public PriceCalculationResponse PriceCalculation(PriceCalculationRequest priceCalculationRequest){
        PriceCalculationResponse priceCalculationResponse = new PriceCalculationResponse();
        // Validate Product and Get Data ....
        ProductRequest productRequest = new ProductRequest();
        productRequest.setProductId(priceCalculationRequest.getProductId());
        ResponseEntity<ProductResponse> responseEntity1 = restTemplate.postForEntity("http://localhost:8083/products", productRequest, ProductResponse.class);
        ProductResponse productResponse = responseEntity1.getBody();
        if(productResponse.getSuccessCode().equals("1003")){
            // Get Calculations ...
            UserDiscountRequest userDiscountRequest = new UserDiscountRequest();
            userDiscountRequest.setUserEmail(priceCalculationRequest.getUserEmail());
            userDiscountRequest.setUserPass(priceCalculationRequest.getUserPass());

            ResponseEntity<UserDiscountResponse> responseEntity2 = restTemplate.postForEntity("http://localhost:8080/discount", userDiscountRequest, UserDiscountResponse.class);
            UserDiscountResponse userDiscountResponse = responseEntity2.getBody();

            if(userDiscountResponse.getSuccessCode().equals("1006")){
                double subTotal = priceCalculationRequest.getProductQuantity() * productResponse.getProductPrice();
                double tax = TaxCalculation(subTotal);
                double margin = 1000.0;
                double totalDiscount = 0;
                if(subTotal > margin){
                    totalDiscount = (subTotal - margin) * userDiscountResponse.getDiscountPercentage();
                }

                priceCalculationResponse.setSubTotal(subTotal);
                priceCalculationResponse.setTax(tax);
                priceCalculationResponse.setTotalDiscount(totalDiscount);
                priceCalculationResponse.setTotal(subTotal-totalDiscount+tax);
                priceCalculationResponse.setSuccessMessage(GetSuccessCode("1004"));
                priceCalculationResponse.setSuccessCode("1004");
            }else{
                System.out.println("PriceCalculation - Price Calculation Error");
                priceCalculationResponse.setSuccessMessage(GetSuccessCode(userDiscountResponse.getSuccessCode()));
                priceCalculationResponse.setSuccessCode(userDiscountResponse.getSuccessCode());
            }
        }else{
            System.out.println("PriceCalculation - Product Error");
            priceCalculationResponse.setSuccessMessage(GetSuccessCode(productResponse.getSuccessCode()));
            priceCalculationResponse.setSuccessCode(productResponse.getSuccessCode());
        }
        return priceCalculationResponse;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
