package com.iliasen.delivcost.configs;

import com.iliasen.delivcost.models.Client;
import com.iliasen.delivcost.models.Driver;
import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.DriverRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final ClientRepository clientRepository;
    private final PartnerRepository partnerRepository;
    private final DriverRepository driverRepository;
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Client client = clientRepository.findByEmail(username).orElse(null);
            if (client != null) {
                return client;
            }

            Partner partner = partnerRepository.findByEmail(username).orElse(null);
            if (partner != null) {
                return partner;
            }

            Driver driver = driverRepository.findByEmail(username).orElse(null);
            if (driver != null) {
                return driver;
            }
            throw new UsernameNotFoundException("User not found");
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}