package com.sliit.mtit.microservice.productservice.impl;

import com.sliit.mtit.microservice.productservice.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ProductServiceImpl {
    @Autowired
    private RestTemplate restTemplate;

    // Static Data used instead of Database since lectures are not going that deep --------
    public static Map<String, String> messagesArr;
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

    // GetProductData API ==================================================================
    public ProductResponse GetProductData(ProductRequest productRequest){
        ProductResponse productResponse = new ProductResponse();
        productResponse.setSuccessMessage(GetSuccessCode("2003"));
        productResponse.setSuccessCode("2003");
        for (var product: productsObjectArr) {
            if(product.getProductId().equals(productRequest.getProductId())){
                productResponse.setProductId(product.getProductId());
                productResponse.setProductName(product.getProductName());
                productResponse.setProductSeller(product.getProductSeller());
                productResponse.setProductPrice(product.getProductPrice());
                productResponse.setSuccessMessage(GetSuccessCode("1003"));
                productResponse.setSuccessCode("1003");
                break;
            }
        }
        return productResponse;
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }
}
