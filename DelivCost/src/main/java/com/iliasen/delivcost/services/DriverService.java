package com.iliasen.delivcost.services;

import com.iliasen.delivcost.dto.DriverDTO;
import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.dto.mapper.DriverMapper;
import com.iliasen.delivcost.dto.mapper.OrderMapper;
import com.iliasen.delivcost.models.Driver;
import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Role;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.repositories.DriverRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.repositories.TransportRepository;
import com.iliasen.delivcost.specification.DriverSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final PartnerRepository partnerRepository;
    private final TransportRepository transportRepository;
    private final DriverMapper driverMapper;
    private final OrderMapper orderMapper;

    @Transactional
    public String addTransportToDriver(Long id, Transport transport) {
        Driver driver = driverRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found")
        );

        Transport existingTransport = transportRepository.findById(transport.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transport not found")
        );

        transport.setDriver(driver);
        transport.setPartner(existingTransport.getPartner());
        transportRepository.save(transport);

        driver.setTransport(transport);
        driverRepository.save(driver);

        return "Transport added successfully";
    }

    public List<OrderDTO> getOrders(Long driverId) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found")
        );
        List<OrderDTO> orders = driver.getOrders().stream().map(orderMapper::toOrderDTO).collect(Collectors.toList());
        return orders;
    }

    public List<DriverDTO> getAll(Pageable pageable, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found")
        );
        return driverRepository.findDriversByPartner(partner, pageable)
                .stream()
                .map(driverMapper::toDriverDTO)
                .collect(Collectors.toList());
    }

    public List<DriverDTO> getFreeDrivers(Pageable pageable, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found")
        );

        return driverRepository.findDriversByPartnerAndTransportIsNull(partner, pageable)
                .stream()
                .map(driverMapper::toDriverDTO)
                .collect(Collectors.toList());
    }

    public List<Driver> filterAndSortDrivers(String firstName, String lastName, String phone, String email, Role role, boolean sortByFirstNameAsc) {
        Specification<Driver> spec = Specification
                .where(DriverSpecification.hasFirstName(firstName))
                .and(DriverSpecification.hasLastName(lastName))
                .and(DriverSpecification.hasPhone(phone))
                .and(DriverSpecification.hasEmail(email))
                .and(DriverSpecification.hasRole(role));

        if (sortByFirstNameAsc) {
            spec = spec.and(DriverSpecification.orderByFirstName(true));
        } else {
            spec = spec.and(DriverSpecification.orderByFirstName(false));
        }

        return driverRepository.findAll(spec);
    }
}
