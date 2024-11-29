package com.iliasen.delivcost.controllers;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.services.TransportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transport")
public class TransportController {
    private final TransportService transportService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Transport transport, @AuthenticationPrincipal UserDetails userDetails) {
        return transportService.addTransport(transport, userDetails);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@AuthenticationPrincipal UserDetails userDetails) {
        return transportService.getTransport(userDetails);
    }
}
