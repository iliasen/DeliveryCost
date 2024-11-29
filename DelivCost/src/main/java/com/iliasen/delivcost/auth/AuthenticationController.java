package com.iliasen.delivcost.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(path = "/singUp/client")
    public ResponseEntity<AuthenticationResponse> registerClient(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(service.registerClient(registerRequest));
    }

    @PostMapping(path = "/singUp/partner")
    public ResponseEntity<AuthenticationResponse> registerPartner(@RequestBody RegisterPartnerRequest registerRequest){
        return ResponseEntity.ok(service.registerPartner(registerRequest));
    }

    @PostMapping(path = "/singUp/driver")
    public ResponseEntity<AuthenticationResponse> registerDriver(@RequestBody RegisterRequest registerRequest, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(service.registerDriver(registerRequest, userDetails));
    }

    @PostMapping(path = "/singIn")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }
}
