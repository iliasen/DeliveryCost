package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.models.Driver;
import com.iliasen.delivcost.models.Order;
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

    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<List<Order>> getAllOrders(@PathVariable Integer id) {
        return driverService.getOrders(id);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateDriver(@PathVariable Integer id, @RequestBody Driver driver) {
        return driverService.update(id, driver);
    }
}
