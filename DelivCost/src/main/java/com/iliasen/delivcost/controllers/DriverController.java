package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.dto.DriverDTO;
import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.models.Role;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.services.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/driver")
@Tag(name = "Drivers", description = "Interactions with drivers")
public class DriverController {

    private final DriverService driverService;

    @Operation(summary = "Get all drivers", description = "Returns a list of all drivers with pagination")
    @GetMapping(value = "/all")
    public ResponseEntity<Page<DriverDTO>> getAllDrivers(
            @PageableDefault(page = 0, size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false, defaultValue = "false") boolean freeOnly,
            @RequestParam(required = false, defaultValue = "false") boolean sortByFirstNameAsc) {
        return ResponseEntity.ok(driverService.getDrivers(pageable, userDetails, freeOnly, firstName, lastName, phone, email, role, sortByFirstNameAsc));
    }

    @Operation(summary = "Get free drivers", description = "Returns a list of free drivers with pagination")
    @GetMapping(value = "/free")
    public ResponseEntity<Page<DriverDTO>> getFreeDrivers(
            @PageableDefault(page = 0, size = 20) Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(driverService.getFreeDrivers(pageable, userDetails));
    }

    @Operation(summary = "Get all orders by driver", description = "Returns a list of all orders for a specific driver")
    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders(
            @PathVariable Long id) {
        return ResponseEntity.ok(driverService.getOrders(id));
    }

    @Operation(summary = "Add transport to driver", description = "Adds a transport to a specific driver")
    @PutMapping(value = "/{id}")
    public ResponseEntity<String> addTransport(
            @PathVariable Long id,
            @RequestBody Transport transport) {
        return ResponseEntity.ok(driverService.addTransportToDriver(id, transport));
    }
}
