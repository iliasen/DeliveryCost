package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.services.WarehouseService;
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
public class WarehouseController {

    private final WarehouseService warehouseService;

    @GetMapping
    public ResponseEntity<Map<String, Integer>> getSpace(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(warehouseService.getFreeSpace(userDetails));

    }
}
