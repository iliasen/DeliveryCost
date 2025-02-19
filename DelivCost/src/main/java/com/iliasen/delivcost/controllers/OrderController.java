package com.iliasen.delivcost.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iliasen.delivcost.dto.MaxWeightDTO;
import com.iliasen.delivcost.dto.OrderAndCargoRequest;
import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.dto.OrderListDTO;
import com.iliasen.delivcost.models.OrderStatus;
import com.iliasen.delivcost.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/order")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderAndCargoRequest request, @PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        OrderDTO orderDTO = orderService.addOrder(request, id, userDetails);
        return ResponseEntity.ok(orderDTO);
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
            @RequestParam(value = "offset", defaultValue = "0")  Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(orderService.getOrders(offset, limit,status,userDetails));
    }

    @GetMapping(value = "/partner")
    @PreAuthorize("hasAuthority('PARTNER')")
    public ResponseEntity<Page<OrderDTO>> getOrdersForPartner(
            @RequestParam(value = "offset", defaultValue = "0")  Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(orderService.getOrdersForPartner(offset, limit, userDetails));
    }

    @GetMapping(value = "/driver/have/{id}")
    public ResponseEntity<List<OrderDTO>> getOrdersForDriver(
            @RequestParam(value = "offset", defaultValue = "0")  Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @PathVariable Long id){
        return ResponseEntity.ok(orderService.getDriverOrders(offset, limit, id));
    }

    @GetMapping(value = "/driver/{id}")
    public ResponseEntity<List<OrderDTO>> getOrdersForTransferToDriver(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(orderService.getOrdersForTransferToDriver(id, userDetails));
    }

    @PostMapping(value = "/transfer/{id}")
    public ResponseEntity<String> transferOrdersToTheDriver(@PathVariable Long id, @RequestBody OrderListDTO orderList){
        return ResponseEntity.ok(orderService.transferOrdersToTheDriver(id, orderList.getOrderList()));
    }

    @PostMapping(value = "/test")
    public ResponseEntity<?> testOrder(@RequestBody OrderListDTO orderListDTO){
        return ResponseEntity.ok(orderListDTO.getOrderList());
    }

    @GetMapping(value = "/new")
    @PreAuthorize("hasAuthority('PARTNER')")
    public ResponseEntity<List<OrderDTO>> getNewOrders(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(orderService.getNewOrders(userDetails));
    }
    
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PutMapping(value = "/status/{id}")
    public ResponseEntity<String> updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status){return ResponseEntity.ok(orderService.updateStatus(id, status));}

    @PreAuthorize("hasAuthority('PARTNER')")
    @PutMapping(value = "/review/{id}")
    public ResponseEntity<String> reviewedOrder(@PathVariable Long id){return ResponseEntity.ok(orderService.setPartnerView(id));}

//    @PreAuthorize("hasAuthority('PARTNER')")
//    @PostMapping(value = "/back_problem")
//    public ResponseEntity<?> BackpackProblem(@RequestBody MaxWeightDTO maxWeightDTO, @AuthenticationPrincipal UserDetails userDetails){
//        return orderService.backpackProblemSolver(maxWeightDTO.getMaxWeight(), userDetails);
//    }
}
