package com.sliit.mtit.microservice.orderservice.controller;
import com.sliit.mtit.microservice.orderservice.dto.OrderRequest;
import com.sliit.mtit.microservice.orderservice.dto.OrderResponse;
import com.sliit.mtit.microservice.orderservice.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderServiceImpl orderService;

    @PostMapping(consumes = "application/json", produces="application/json")
    public @ResponseBody OrderResponse createOrder(@RequestBody OrderRequest request){
        System.out.println("Order Details" + request);
        return orderService.CreateOrder(request);
    }
}