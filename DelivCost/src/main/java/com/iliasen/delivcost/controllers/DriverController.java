package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.DriverDTO;
import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.services.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/driver")
public class DriverController {

    private final DriverService driverService;

    @GetMapping(value = "/all")
    public ResponseEntity<List<DriverDTO>> getAllDrivers(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(driverService.getAll(userDetails));
    }

    @GetMapping(value = "/free")
    public ResponseEntity<List<DriverDTO>> getFreeDrivers(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(driverService.getFreeDrivers(userDetails));
    }

    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.getOrders(id));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> addTransport(@PathVariable Long id, @RequestBody Transport transport) {
        return ResponseEntity.ok(driverService.addTransportToDriver(id, transport));
    }
}
