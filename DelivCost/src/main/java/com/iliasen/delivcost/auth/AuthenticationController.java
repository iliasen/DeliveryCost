package com.iliasen.delivcost.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/auth")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping(path = "/singUp/client")
    public ResponseEntity<AuthenticationResponse> registerClient(@RequestBody RegisterClientRequest registerRequest){
        return ResponseEntity.ok(service.registerClient(registerRequest));
    }

    @PostMapping(path = "/singUp/partner")
    public ResponseEntity<AuthenticationResponse> registerPartner(@RequestBody RegisterPartnerRequest registerRequest){
        return ResponseEntity.ok(service.registerPartner(registerRequest));
    }

    @PostMapping(path = "/singIn")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(service.authenticate(authenticationRequest));
    }
}
