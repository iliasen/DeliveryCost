package com.iliasen.delivcost.auth;

import com.iliasen.delivcost.configs.JwtService;
import com.iliasen.delivcost.exeptions.UserNotFoundException;
import com.iliasen.delivcost.models.Client;
import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Role;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ClientRepository clientRepository;
    private final PartnerRepository partnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerClient(RegisterClientRequest request) {
        var client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .build();

        clientRepository.save(client);
        var jwtToken = jwtService.generateToken(client);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse registerPartner(RegisterPartnerRequest request) {
        var partner = Partner.builder()
                .companyName(request.getCompanyName())
                .inn(request.getInn())
                .contactNumber(request.getContactNumber())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PARTNER)
                .build();

        partnerRepository.save(partner);
        var jwtToken = jwtService.generateToken(partner);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String jwtToken = jwtService.generateToken(userDetails);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}