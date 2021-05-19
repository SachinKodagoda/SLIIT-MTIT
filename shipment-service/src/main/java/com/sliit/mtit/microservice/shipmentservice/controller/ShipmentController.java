package com.sliit.mtit.microservice.shipmentservice.controller;

import com.sliit.mtit.microservice.shipmentservice.dto.*;
import com.sliit.mtit.microservice.shipmentservice.impl.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/shipment")
public class ShipmentController {

    @Autowired
    private ShipmentServiceImpl shipmentServiceImpl;

    @PostMapping(consumes = "application/json", produces="application/json")
    public @ResponseBody ShipmentResponse shipmentController(@RequestBody ShipmentRequest request){
        return shipmentServiceImpl.GetShipmentData(request);
    }
}