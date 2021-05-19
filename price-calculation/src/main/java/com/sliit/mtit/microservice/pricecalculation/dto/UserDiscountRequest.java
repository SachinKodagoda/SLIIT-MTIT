package com.sliit.mtit.microservice.pricecalculation.dto;

public class UserDiscountRequest {
    private String userEmail;
    private String userPass;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }
}
