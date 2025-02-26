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

    public List<TransportDTO> getTransportByType(String type, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        List<Transport> transportList;

        if (type != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(type);

                String transportTypeString = jsonNode.path("type").asText();

                TransportType transportType = TransportType.valueOf(transportTypeString);
                transportList = transportRepository.findByPartnerIdAndTransportType(partner.getId(), transportType);
                transportList = transportList.stream()
                        .filter(transport -> transport.getDriver() != null)
                        .collect(Collectors.toList());

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error parsing transport type", e);
            }
        } else {
            transportList = transportRepository.findByPartnerId(partner.getId());
        }

        if (transportList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transport found");
        }

        return transportList.stream().map(transportMapper::toTransportDTO).collect(Collectors.toList());
    }


    public Page<TransportDTO> getTransport(Pageable pageable, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        Page<Transport> transportList = transportRepository.findByPartnerId(partner.getId(), pageable);
        if (transportList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transport found");
        }
        return transportList.map(transportMapper::toTransportDTO);
    }


    public List<TransportDTO> getTransportForUser(Long id) {
        partnerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        List<Transport> transportList = transportRepository.findByPartnerId(id);
        transportList = transportList.stream()
                .filter(transport -> transport.getDriver() != null)
                .collect(Collectors.toList());
        if (transportList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No transport found");
        }

        // Фильтрация по уникальным типам транспорта
        Map<TransportType, Transport> uniqueTransportMap = transportList.stream()
                .collect(Collectors.toMap(
                        Transport::getTransportType,
                        transport -> transport,
                        (existing, replacement) -> existing
                ));

        return uniqueTransportMap.values().stream()
                .map(transportMapper::toTransportDTO)
                .collect(Collectors.toList());
    }

    public List<Transport> filterAndSortTransports(TransportType transportType, double minTonnage, double minVolume, Partner partner, boolean sortByTonnageAsc) {
        Specification<Transport> spec = Specification
                .where(TransportSpecification.hasTransportType(transportType))
                .and(TransportSpecification.hasTonnageGreaterThan(minTonnage))
                .and(TransportSpecification.hasVolumeGreaterThan(minVolume))
                .and(TransportSpecification.hasPartner(partner));

        if (sortByTonnageAsc) {
            spec = spec.and(TransportSpecification.orderByTonnage(true));
        } else {
            spec = spec.and(TransportSpecification.orderByTonnage(false));
        }

        return transportRepository.findAll(spec);
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