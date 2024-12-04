package com.iliasen.delivcost.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliasen.delivcost.models.*;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.repositories.TransportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransportService {
    private final TransportRepository transportRepository;
    private final PartnerRepository partnerRepository;

    public ResponseEntity<?> addTransport(Transport transport, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        transport.setPartner(partner);
        transportRepository.save(transport);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> getTransportByType(String type, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        try {
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
                    e.printStackTrace();
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }

            } else {
                transportList = transportRepository.findByPartnerId(partner.getId());
            }

            if (transportList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(transportList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getTransport(UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        try {
            List<Transport> transportList = transportRepository.findByPartnerId(partner.getId());
            return transportList.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(transportList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getTransportForUser(Integer id) {
        partnerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        try {
            List<Transport> transportList = transportRepository.findByPartnerId(id);
            transportList = transportList.stream()
                    .filter(transport -> transport.getDriver() != null)
                    .collect(Collectors.toList());
            return transportList.isEmpty() ? new ResponseEntity<>(HttpStatus.NOT_FOUND) : new ResponseEntity<>(transportList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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