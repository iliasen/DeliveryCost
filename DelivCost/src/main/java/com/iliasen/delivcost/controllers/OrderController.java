package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.models.Cargo;
import com.iliasen.delivcost.models.Order;
import com.iliasen.delivcost.models.OrderStatus;
import com.iliasen.delivcost.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> createOrder(@RequestBody Order orderRequest, @RequestBody Cargo cargoRequest, @PathVariable Integer id){
        return orderService.addOrder(orderRequest, cargoRequest, id);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(@RequestBody(required = false) String status){
        return orderService.getOrders(status);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Integer id){return orderService.getOrder(id);}

    @PutMapping(value = "/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id, @RequestBody OrderStatus newStatus){return orderService.updateStatus(id, newStatus);}
}
