package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.MaxWeightDTO;
import com.iliasen.delivcost.dto.OrderAndCargoRequest;
import com.iliasen.delivcost.dto.OrderListDTO;
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
//        System.out.println(request);
        Order orderRequest = request.getOrder();
        Cargo cargoRequest = request.getCargo();
        return orderService.addOrder(orderRequest, cargoRequest, id, userDetails);
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

    @GetMapping(value = "/driver/{id}")
    public ResponseEntity<?> getOrderForDriver(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails){
        return orderService.getOrdersForDriver(id, userDetails);
    }

    @PostMapping(value = "/transfer/{id}")
    public ResponseEntity<?> transferOrdersToTheDriver(@PathVariable Integer id, @RequestBody OrderListDTO orderList){
        System.out.println(orderList.toString());
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
    public ResponseEntity<?> getOrder(@PathVariable Integer id){return orderService.getOrder(id);}

    @PutMapping(value = "/status/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Integer id, @RequestParam OrderStatus status){return orderService.updateStatus(id, status);}

    @PreAuthorize("hasAuthority('PARTNER')")
    @PutMapping(value = "/review/{id}")
    public ResponseEntity<?> reviewedOrder(@PathVariable Integer id){return orderService.setPartnerView(id);}

    @PreAuthorize("hasAuthority('PARTNER')")
    @PostMapping(value = "/back_problem")
    public ResponseEntity<?> BackpackProblem(@RequestBody MaxWeightDTO maxWeightDTO, @AuthenticationPrincipal UserDetails userDetails){
        return orderService.backpackProblemSolver(maxWeightDTO.getMaxWeight(), userDetails);
    }
}
