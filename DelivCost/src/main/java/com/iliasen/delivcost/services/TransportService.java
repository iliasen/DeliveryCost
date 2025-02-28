package com.iliasen.delivcost.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliasen.delivcost.dto.TransportDTO;
import com.iliasen.delivcost.dto.mapper.TransportMapper;
import com.iliasen.delivcost.models.*;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.repositories.TransportRepository;
import com.iliasen.delivcost.specification.TransportSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportService {
    private final TransportRepository transportRepository;
    private final PartnerRepository partnerRepository;
    private final TransportMapper transportMapper;

    public String addTransport(Transport transport, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        transport.setPartner(partner);
        transportRepository.save(transport);
        return "Transport created";
    }

    public Page<TransportDTO> getTransport(
            Pageable pageable,
            UserDetails userDetails,
            Long partnerId,
            String transportTypeStr,
            Boolean onlyWithDriver,
            Boolean uniqueTypes) {

        try {
            Specification<Transport> spec = Specification.where(null);

            if (partnerId == null && userDetails != null) {
                Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));
                partnerId = partner.getId();
            }

            spec = spec.and(TransportSpecification.belongsToPartner(partnerId));

            if (transportTypeStr != null && !transportTypeStr.isEmpty()) {
                try {
                    TransportType transportType = TransportType.valueOf(transportTypeStr.toUpperCase());
                    spec = spec.and(TransportSpecification.hasTransportType(transportType));
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transport type");
                }
            }

            if (onlyWithDriver != null && onlyWithDriver) {
                spec = spec.and(TransportSpecification.hasDriver());
            }

            if (uniqueTypes != null && uniqueTypes) {
                spec = spec.and(TransportSpecification.uniqueTransportTypes());
            }

            Page<Transport> transportPage = transportRepository.findAll(spec, pageable);

            if (transportPage.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transport found");
            }

            return transportPage.map(transportMapper::toTransportDTO);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }



    public boolean calculateVolume(Transport transport, List<Order> orders) {
        List<Cargo> cargos = orders.stream()
                .map(Order::getCargo)
                .collect(Collectors.toList());

        double totalVolume = 0.0;

        for (Cargo cargo : cargos) {
            double volume = cargo.getVolume();
            totalVolume += volume;
        }

        return transport.getVolume() > totalVolume;
    }

}