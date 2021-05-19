package com.sliit.mtit.microservice.orderservice.dto;

public class OrderResponse {
    private String successMessage;
    private String successCode;
    private String shippingTrackId;
    private String possibleShippingDate;
    private String shippingAddress;
    private double subTotal;
    private double tax;
    private double totalDiscount;
    private double total;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getSuccessCode() {
        return successCode;
    }

    public void setSuccessCode(String successCode) {
        this.successCode = successCode;
    }

    public String getShippingTrackId() {
        return shippingTrackId;
    }

    public void setShippingTrackId(String shippingTrackId) {
        this.shippingTrackId = shippingTrackId;
    }

    public String getPossibleShippingDate() {
        return possibleShippingDate;
    }

    public void setPossibleShippingDate(String possibleShippingDate) {
        this.possibleShippingDate = possibleShippingDate;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
