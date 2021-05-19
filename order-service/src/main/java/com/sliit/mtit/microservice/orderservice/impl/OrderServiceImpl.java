package com.sliit.mtit.microservice.orderservice.impl;

import com.sliit.mtit.microservice.orderservice.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
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
        messagesArr.put("1004","Successfully price calculated");
        messagesArr.put("1005","Successfully shipment data received");
        messagesArr.put("1006","Successfully discount calculated");

        messagesArr.put("2001","Order couldn't create.try again!");
        messagesArr.put("2002","User name or password is wrong.try again!");
        messagesArr.put("2003","Unavailable product.try again!");
        messagesArr.put("2004","Unavailable product so couldn't calculate price.try again!");
        messagesArr.put("2005","Shipment data didn't received.try again!");

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
        userDetailRequest.setUserPass(shipmentRequest.getUserPass());
        userDetailRequest.setUserEmail(shipmentRequest.getUserEmail());
        UserDetailResponse userDetailResponse = GetUserData(userDetailRequest);
        if(userDetailResponse.getSuccessCode().equals("1002")){
            // Set Shipment data
            shipmentResponse.setShippingAddress(userDetailResponse.getUserShippingAddress());
            shipmentResponse.setPossibleShippingDate(GetCurrentDate());
            shipmentResponse.setShippingTrackId(UUID.randomUUID().toString());
            shipmentResponse.setSuccessMessage(GetSuccessCode("1005"));
            shipmentResponse.setSuccessCode("1005");
        }else{
            shipmentResponse.setSuccessMessage(GetSuccessCode(userDetailResponse.getSuccessCode()));
            shipmentResponse.setSuccessCode(userDetailResponse.getSuccessCode());
        }
        return shipmentResponse;
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
        ProductResponse productResponse = GetProductData(productRequest);
        if(productResponse.getSuccessCode().equals("1003")){
            UserDiscountRequest userDiscountRequest = new UserDiscountRequest();
            userDiscountRequest.setUserEmail(priceCalculationRequest.getUserEmail());
            UserDiscountResponse userDiscountResponse = UserDiscount(userDiscountRequest);
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
                priceCalculationResponse.setSuccessMessage(GetSuccessCode(userDiscountResponse.getSuccessCode()));
                priceCalculationResponse.setSuccessCode(userDiscountResponse.getSuccessCode());
            }
        }else{
            priceCalculationResponse.setSuccessMessage(GetSuccessCode(productResponse.getSuccessCode()));
            priceCalculationResponse.setSuccessCode(productResponse.getSuccessCode());
        }
        return priceCalculationResponse;
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
        UserDetailResponse userDetailResponse =  GetUserData(userDetailRequest);
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
        }else{
            userDiscountResponse.setSuccessMessage(GetSuccessCode(userDetailResponse.getSuccessCode()));
            userDiscountResponse.setSuccessCode(userDetailResponse.getSuccessCode());
        }
        return userDiscountResponse;
    }


    // CreateOrder API ==================================================================
    public OrderResponse CreateOrder(OrderRequest orderRequest){
        // This is the order response object
        OrderResponse orderResponse = new OrderResponse();

        // Validate User ....
        UserDetailRequest userDetailRequest = new UserDetailRequest();
        userDetailRequest.setUserEmail(orderRequest.getUserEmail());
        userDetailRequest.setUserPass(orderRequest.getUserPass());
        UserDetailResponse userDetailResponse =  GetUserData(userDetailRequest);

        if(userDetailResponse.getSuccessCode().equals("1002") ){
            // Get Shipping Data ....
            ShipmentRequest shipmentRequest = new ShipmentRequest();
            shipmentRequest.setUserEmail(orderRequest.getUserEmail());
            ShipmentResponse shipmentResponse = GetShipmentData(shipmentRequest);

            if(shipmentResponse.getSuccessCode().equals("1005")){
                // Set Shipping Data
                orderResponse.setShippingTrackId(shipmentResponse.getShippingTrackId());
                orderResponse.setShippingAddress(shipmentResponse.getShippingAddress());
                orderResponse.setPossibleShippingDate(shipmentResponse.getPossibleShippingDate());

                // Get Product Details ....
                ProductRequest productRequest = new ProductRequest();
                productRequest.setProductId(orderRequest.getProductId());
                ProductResponse productResponse = GetProductData(productRequest);

                if(productResponse.getSuccessCode().equals("1003")){
                    // Set Order Response
                    orderResponse.setProductQuantity(orderRequest.getProductQuantity());
                    orderResponse.setProductName(productResponse.getProductName());
                    orderResponse.setProductId(productResponse.getProductId());
                    orderResponse.setProductSeller(productResponse.getProductSeller());
                    orderResponse.setProductPrice(productResponse.getProductPrice());

                    // Get Calculated price ....
                    PriceCalculationRequest priceCalculationRequest = new PriceCalculationRequest();
                    priceCalculationRequest.setProductId(orderRequest.getProductId());
                    priceCalculationRequest.setProductQuantity(orderRequest.getProductQuantity());
                    priceCalculationRequest.setUserEmail(orderRequest.getUserEmail());
                    PriceCalculationResponse priceCalculationResponse = PriceCalculation(priceCalculationRequest);
                    if(priceCalculationResponse.getSuccessCode().equals("1004")){
                        // Set Calculated price
                        orderResponse.setTax(priceCalculationResponse.getTax());
                        orderResponse.setSubTotal(priceCalculationResponse.getSubTotal());
                        orderResponse.setTotal(priceCalculationResponse.getTotal());
                        orderResponse.setTotalDiscount(priceCalculationResponse.getTotalDiscount());
                        // Request is totally successful
                        orderResponse.setSuccessMessage(GetSuccessCode("1001"));
                        orderResponse.setSuccessCode("1001");
                        orderResponse.setOrderId(UUID.randomUUID().toString());

                    }else{
                        orderResponse.setSuccessMessage(GetSuccessCode(priceCalculationResponse.getSuccessCode()));
                        orderResponse.setSuccessCode(priceCalculationResponse.getSuccessCode());
                    }
                }else{
                    orderResponse.setSuccessMessage(GetSuccessCode(productResponse.getSuccessCode()));
                    orderResponse.setSuccessCode(productResponse.getSuccessCode());
                }
            }else{
                orderResponse.setSuccessMessage(GetSuccessCode(shipmentResponse.getSuccessCode()));
                orderResponse.setSuccessCode(shipmentResponse.getSuccessCode());
            }
        }else{
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
