package com.iliasen.delivcost.controllers;
import com.iliasen.delivcost.dto.TransportDTO;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.services.TransportService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transport")
@Tag(name = "Transport", description = "Interactions with transport")
public class TransportController {
    private final TransportService transportService;

    @Operation(summary = "Create transport", description = "Creates a new transport entity")
    @PostMapping
    public ResponseEntity<String> create(
            @RequestBody Transport transport,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(transportService.addTransport(transport, userDetails));
    }

    @Operation(summary = "Get transport", description = "Returns a list of transport entities based on filters")
    @GetMapping
    public ResponseEntity<Page<TransportDTO>> getTransport(
            @PageableDefault(page = 0, size = 20) Pageable pageable,
            @RequestParam(required = false) String transportType,
            @RequestParam(required = false) Long partnerId,
            @RequestParam(required = false) Boolean onlyWithDriver,
            @RequestParam(required = false) Boolean uniqueTypes,
            @AuthenticationPrincipal UserDetails userDetails) {

        Page<TransportDTO> transports = transportService.getTransport(
                pageable,
                userDetails,
                partnerId,
                transportType,
                onlyWithDriver,
                uniqueTypes
        );

        return ResponseEntity.ok(transports);
    }
}
