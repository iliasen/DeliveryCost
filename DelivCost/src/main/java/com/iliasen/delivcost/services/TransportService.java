package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.repositories.TransportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
}