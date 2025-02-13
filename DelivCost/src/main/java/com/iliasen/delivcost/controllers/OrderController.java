package com.iliasen.delivcost.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iliasen.delivcost.dto.MaxWeightDTO;
import com.iliasen.delivcost.dto.OrderAndCargoRequest;
import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.dto.OrderListDTO;
import com.iliasen.delivcost.mapper.OrderMapper;
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

    private final OrderMapper orderMapper;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderAndCargoRequest request, @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        OrderDTO orderDTO = orderService.addOrder(request, id, userDetails);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders(@RequestParam(required = false) String status,@AuthenticationPrincipal UserDetails userDetails){
        return orderService.getOrders(status,userDetails);
    }

    @GetMapping(value = "/partner")
    @PreAuthorize("hasAuthority('PARTNER')")
    public ResponseEntity<?> getOrdersForPartner(@AuthenticationPrincipal UserDetails userDetails){
        return orderService.getOrdersForPartner(userDetails);
    }

    @GetMapping(value = "/driver/have/{id}")
    public ResponseEntity<?> getOrdersForDriver(@PathVariable Long id){
        return orderService.getDriverOrders(id);
    }

    @GetMapping(value = "/driver/{id}")
    public ResponseEntity<?> getOrdersForTransferToDriver(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return orderService.getOrdersForTransferToDriver(id, userDetails);
    }

    @PostMapping(value = "/transfer/{id}")
    public ResponseEntity<?> transferOrdersToTheDriver(@PathVariable Long id, @RequestBody OrderListDTO orderList){
        return orderService.transferOrdersToTheDriver(id, orderList.getOrderList());
    }

    @PostMapping(value = "/test")
    public ResponseEntity<?> testOrder(@RequestBody OrderListDTO orderListDTO){
        return ResponseEntity.ok(orderListDTO.getOrderList());
    }

    @GetMapping(value = "/new")
    @PreAuthorize("hasAuthority('PARTNER')")
    public ResponseEntity<?> getNewOrders(@AuthenticationPrincipal UserDetails userDetails){
        return orderService.getNewOrders(userDetails);
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id){return orderService.getOrder(id);}

    @PutMapping(value = "/status/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status){return orderService.updateStatus(id, status);}

    @PreAuthorize("hasAuthority('PARTNER')")
    @PutMapping(value = "/review/{id}")
    public ResponseEntity<?> reviewedOrder(@PathVariable Long id){return orderService.setPartnerView(id);}

    @PreAuthorize("hasAuthority('PARTNER')")
    @PostMapping(value = "/back_problem")
    public ResponseEntity<?> BackpackProblem(@RequestBody MaxWeightDTO maxWeightDTO, @AuthenticationPrincipal UserDetails userDetails){
        return orderService.backpackProblemSolver(maxWeightDTO.getMaxWeight(), userDetails);
    }
}
