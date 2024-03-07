package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.OrderAndCargoRequest;
import com.iliasen.delivcost.models.Cargo;
import com.iliasen.delivcost.models.Order;
import com.iliasen.delivcost.models.OrderStatus;
import com.iliasen.delivcost.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/order")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/{id}")
    public ResponseEntity<?> createOrder(@RequestBody OrderAndCargoRequest request, @PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails){
        Order orderRequest = request.getOrder();
        Cargo cargoRequest = request.getCargo();
        return orderService.addOrder(orderRequest, cargoRequest, id, userDetails);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(@RequestParam(required = false) String status,@AuthenticationPrincipal UserDetails userDetails){
        return orderService.getOrders(status,userDetails);
    }

    @PreAuthorize("hasAuthority('PARTNER')")
    @GetMapping(value = "/new")
    public ResponseEntity<?> getNewOrders(@AuthenticationPrincipal UserDetails userDetails){
        return orderService.getNewOrders(userDetails);
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Integer id){return orderService.getOrder(id);}

    @PreAuthorize("hasAuthority('PARTNER')")
    @PutMapping(value = "/status/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id, @RequestParam OrderStatus status){return orderService.updateStatus(id, status);}

    @PreAuthorize("hasAuthority('PARTNER')")
    @PutMapping(value = "/review/{id}")
    public ResponseEntity<?> reviewedOrder(@PathVariable Integer id){return orderService.setPartnerView(id);}

}
