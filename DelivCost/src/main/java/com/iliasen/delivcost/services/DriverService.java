package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.Driver;
import com.iliasen.delivcost.models.Order;
import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.repositories.DriverRepository;
import com.iliasen.delivcost.repositories.OrderRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final PartnerRepository partnerRepository;
    private final OrderRepository orderRepository;

    public ResponseEntity<?> update(Integer id, Driver updatedDriver) {
        driverRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found")
        );
        driverRepository.save(updatedDriver);
        return ResponseEntity.ok("Transport added successfully");
    }

    public ResponseEntity<List<Order>> getOrders(Integer driverId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found")
        );
        List<Order> orders = driver.getOrders();
        return ResponseEntity.ok(orders);
    }

    public ResponseEntity<List<Driver>> getAll(UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Driver> drivers = driverRepository.findDriversByPartner(partner);
        return ResponseEntity.ok(drivers);
    }
}
