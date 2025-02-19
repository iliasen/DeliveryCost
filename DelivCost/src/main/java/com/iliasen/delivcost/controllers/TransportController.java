package com.iliasen.delivcost.controllers;
import com.iliasen.delivcost.dto.TransportDTO;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transport")
public class TransportController {
    private final TransportService transportService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody Transport transport, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(transportService.addTransport(transport, userDetails));
    }

    @GetMapping
    public ResponseEntity<List<TransportDTO>> getAll(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                     @RequestParam(value = "limit", defaultValue = "10") Integer limit,
                                                     @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(transportService.getTransport(offset, limit, userDetails));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<List<TransportDTO>> getForUser(@PathVariable Long id) {
        return ResponseEntity.ok(transportService.getTransportForUser(id));
    }

    @GetMapping(value = "/by_type")
    public ResponseEntity<List<TransportDTO>> getByType(@RequestBody(required = false) String type, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(transportService.getTransportByType(type, userDetails));
    }
}
