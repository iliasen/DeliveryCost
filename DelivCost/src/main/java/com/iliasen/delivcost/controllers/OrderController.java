package com.iliasen.delivcost.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.iliasen.delivcost.dto.MaxWeightDTO;
import com.iliasen.delivcost.dto.OrderAndCargoRequest;
import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.dto.OrderListDTO;
import com.iliasen.delivcost.models.OrderStatus;
import com.iliasen.delivcost.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Orders", description = "Interactions with orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Create a new order", description = "Creates a new order for a specific user and cargo")
    @PostMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderAndCargoRequest request,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        OrderDTO orderDTO = orderService.addOrder(request, id, userDetails);
        return ResponseEntity.ok(orderDTO);
    }

    @Operation(summary = "Get all orders", description = "Returns a paginated list of all orders with optional status filter")
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(required = false) String status,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrders(offset, limit, status, userDetails));
    }

    @PreAuthorize("hasAuthority('PARTNER')")
    @Operation(summary = "Get orders for partner", description = "Returns a paginated list of orders for a specific partner")
    @GetMapping(value = "/partner")
    public ResponseEntity<Page<OrderDTO>> getOrdersForPartner(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrdersForPartner(offset, limit, userDetails));
    }

    @Operation(summary = "Get orders for driver", description = "Returns a list of orders assigned to a specific driver with pagination")
    @GetMapping(value = "/driver/have/{id}")
    public ResponseEntity<List<OrderDTO>> getOrdersForDriver(
            @RequestParam(value = "offset", defaultValue = "0") Integer offset,
            @RequestParam(value = "limit", defaultValue = "20") Integer limit,
            @PathVariable Long id) {
        return ResponseEntity.ok(orderService.getDriverOrders(offset, limit, id));
    }

    @Operation(summary = "Get orders available for transfer to driver", description = "Returns a list of orders available for transfer to a specific driver")
    @GetMapping(value = "/driver/{id}")
    public ResponseEntity<List<OrderDTO>> getOrdersForTransferToDriver(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getOrdersForTransferToDriver(id, userDetails));
    }

    @Operation(summary = "Transfer orders to driver", description = "Transfers a list of orders to a specific driver")
    @PostMapping(value = "/transfer/{id}")
    public ResponseEntity<String> transferOrdersToTheDriver(
            @PathVariable Long id,
            @RequestBody OrderListDTO orderList) {
        return ResponseEntity.ok(orderService.transferOrdersToTheDriver(id, orderList.getOrderList()));
    }

    @Operation(summary = "Test order", description = "Endpoint for testing order functionality")
    @PostMapping(value = "/test")
    public ResponseEntity<?> testOrder(@RequestBody OrderListDTO orderListDTO) {
        return ResponseEntity.ok(orderListDTO.getOrderList());
    }

    @PreAuthorize("hasAuthority('PARTNER')")
    @Operation(summary = "Get new orders", description = "Returns a list of new orders for a specific partner")
    @GetMapping(value = "/new")
    public ResponseEntity<List<OrderDTO>> getNewOrders(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(orderService.getNewOrders(userDetails));
    }

    @Operation(summary = "Get order by ID", description = "Returns a specific order by its ID")
    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @Operation(summary = "Update order status", description = "Updates the status of a specific order")
    @PutMapping(value = "/status/{id}")
    public ResponseEntity<String> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateStatus(id, status));
    }

    @PreAuthorize("hasAuthority('PARTNER')")
    @Operation(summary = "Mark order as reviewed", description = "Marks a specific order as reviewed by the partner")
    @PutMapping(value = "/review/{id}")
    public ResponseEntity<String> reviewedOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.setPartnerView(id));
    }
//    @PreAuthorize("hasAuthority('PARTNER')")
//    @PostMapping(value = "/back_problem")
//    public ResponseEntity<?> BackpackProblem(@RequestBody MaxWeightDTO maxWeightDTO, @AuthenticationPrincipal UserDetails userDetails){
//        return orderService.backpackProblemSolver(maxWeightDTO.getMaxWeight(), userDetails);
//    }
}
