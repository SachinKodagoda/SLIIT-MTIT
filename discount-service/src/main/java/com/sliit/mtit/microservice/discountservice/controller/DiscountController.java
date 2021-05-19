package com.sliit.mtit.microservice.discountservice.controller;

import com.sliit.mtit.microservice.discountservice.dto.*;
import com.sliit.mtit.microservice.discountservice.impl.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountServiceImpl discountServiceImpl;

    @PostMapping(consumes = "application/json", produces="application/json")
    public @ResponseBody UserDiscountResponse discountController(@RequestBody UserDiscountRequest request){
        return discountServiceImpl.UserDiscount(request);
    }
}