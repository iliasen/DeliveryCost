package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.*;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.OrderRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CargoService cargoService;

    private final OrderRepository orderRepository;
    private final PartnerRepository partnerRepository;
    private final ClientRepository clientRepository;


    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addOrder(Order order,Cargo cargo, Integer partnerId, UserDetails userDetails) {

        Client client = clientRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));


        order.setClient(client);
        order.setPartner(partner);
        order.setCargo(cargo);
        orderRepository.save(order);
        cargoService.addCargo(cargo, order);

        return ResponseEntity.ok("order added");
    }

    public  ResponseEntity<?> getOrders(String status, UserDetails userDetails) {
        List<Order> orders = new ArrayList<>();

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PARTNER"))) {
            Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Partner not found"));

            orders = orderRepository.findByPartnerId(partner.getId());
        } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
            Client client = clientRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Client not found"));

            orders = orderRepository.findByClientId(client.getId());
        }

        if (status != null) {
            OrderStatus filterStatus = OrderStatus.valueOf(status);
            orders = orders.stream()
                    .filter(order -> order.getOrderStatus() == filterStatus)
                    .collect(Collectors.toList());
        }

        Collections.sort(orders, Comparator.comparing(
                Order::getOrderStatus,
                Comparator.nullsLast(Comparator.comparing(OrderStatus::ordinal))
        ));
        if (!orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        }

        return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> getOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return ResponseEntity.ok(order);
    }


    public ResponseEntity<?> updateStatus(Integer id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (newStatus == OrderStatus.DELIVERED) {
            cargoService.transferCargoToWarehouse(order);
        }

        order.setOrderStatus(newStatus);
        orderRepository.save(order);
        return ResponseEntity.ok("Status update");
    }
}
