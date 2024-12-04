package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.models.Driver;
import com.iliasen.delivcost.models.Order;
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
    public ResponseEntity<List<Driver>> getAllDrivers(@AuthenticationPrincipal UserDetails userDetails) {
        return driverService.getAll(userDetails);
    }

    @GetMapping(value = "/free")
    public ResponseEntity<List<Driver>> getFreeDrivers(@AuthenticationPrincipal UserDetails userDetails) {
        return driverService.getFreeDrivers(userDetails);
    }

    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<List<Order>> getAllOrders(@PathVariable Integer id) {
        return driverService.getOrders(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> addTransport(@PathVariable Integer id, @RequestBody Transport transport) {
        return driverService.addTransportToDriver(id, transport);
    }
}
