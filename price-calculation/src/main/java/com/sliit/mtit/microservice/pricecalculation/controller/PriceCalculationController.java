package com.sliit.mtit.microservice.pricecalculation.controller;

import com.sliit.mtit.microservice.pricecalculation.dto.*;
import com.sliit.mtit.microservice.pricecalculation.impl.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/price")
public class PriceCalculationController {

    @Autowired
    private PriceCalculationServiceImpl priceCalculationServiceImpl;

    @PostMapping(consumes = "application/json", produces="application/json")
    public @ResponseBody PriceCalculationResponse priceCalculationController(@RequestBody PriceCalculationRequest request){
        return priceCalculationServiceImpl.PriceCalculation(request);
    }
}