package com.sliit.mtit.microservice.orderservice.dto;

public class Product {
    private String productId;
    private String productName;
    private double productPrice;
    private String productSeller;

    public Product(String productId, String productName, double productPrice, String productSeller) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productSeller = productSeller;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSeller() {
        return productSeller;
    }

    public void setProductSeller(String productSeller) {
        this.productSeller = productSeller;
    }
}
