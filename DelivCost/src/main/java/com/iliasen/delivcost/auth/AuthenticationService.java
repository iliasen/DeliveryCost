package com.iliasen.delivcost.auth;

import com.iliasen.delivcost.configs.JwtService;
import com.iliasen.delivcost.exeptions.UserNotFoundException;
import com.iliasen.delivcost.models.*;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.DriverRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.repositories.WarehouseRepository;
import org.springframework.security.core.GrantedAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final ClientRepository clientRepository;
    private final PartnerRepository partnerRepository;
    private final DriverRepository driverRepository;
    private final WarehouseRepository storageRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerClient(RegisterRequest request) {

        String email = request.getEmail();
        if (clientRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        var client = Client.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CLIENT)
                .build();

        Warehouse warehouse = new Warehouse();
        client.setWarehouse(warehouse);

        clientRepository.save(client);
        warehouse.setClient(client);
        storageRepository.save(warehouse);

        var jwtToken = jwtService.generateToken(client, client.getRole());
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
        var jwtToken = jwtService.generateToken(partner, partner.getRole());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse registerDriver(RegisterRequest registerRequest, UserDetails userDetails) {
        System.out.println(userDetails.getUsername());
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Partner not found")
        );

        String email = registerRequest.getEmail();
        if (driverRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        var driver = Driver.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .phone(registerRequest.getPhone())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .partner(partner)
                .role(Role.DRIVER)
                .build();

        partner.getDriverList().add(driver);

        driverRepository.save(driver);

        partnerRepository.save(partner);

        var jwtToken = jwtService.generateToken(driver, driver.getRole());
        return AuthenticationResponse.builder().
                token(jwtToken)
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

            // Получение роли пользователя
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElseThrow(() -> new IllegalStateException("У пользователя нет ролей"));

            String jwtToken = switch (role) {
                case "CLIENT" -> jwtService.generateToken(userDetails, Role.CLIENT);
                case "PARTNER" -> jwtService.generateToken(userDetails, Role.PARTNER);
                case "DRIVER" -> jwtService.generateToken(userDetails, Role.DRIVER);
                default -> throw new IllegalStateException("Недопустимая роль пользователя: " + role);
            };

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            throw new UserNotFoundException("Пользователь не найден");
        }
    }
}