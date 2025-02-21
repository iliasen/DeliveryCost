package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.services.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/warehouse")
@Tag(name = "Warehouse", description = "Interactions with warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Operation(summary = "Get free warehouse space", description = "Returns the free space available in the warehouse for the authenticated user")
    @GetMapping
    public ResponseEntity<Map<String, Integer>> getSpace(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(warehouseService.getFreeSpace(userDetails));
    }
}