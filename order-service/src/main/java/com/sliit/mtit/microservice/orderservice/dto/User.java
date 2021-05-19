package com.sliit.mtit.microservice.orderservice.dto;

public class User {
    private String userName;
    private String userShippingAddress;
    private String userEmail;
    private String userContact;
    private String password;
    private String userType;

    public User(String userName, String userShippingAddress, String userEmail, String userContact, String password, String userType) {
        this.userName = userName;
        this.userShippingAddress = userShippingAddress;
        this.userEmail = userEmail;
        this.userContact = userContact;
        this.password = password;
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserShippingAddress() {
        return userShippingAddress;
    }

    public void setUserShippingAddress(String userShippingAddress) {
        this.userShippingAddress = userShippingAddress;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
