package com.sliit.mtit.microservice.orderservice.dto;

public class ShipmentResponse {
    private String successMessage;
    private String successCode;
    private String shippingTrackId;
    private String possibleShippingDate;
    private String shippingAddress;

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
}
