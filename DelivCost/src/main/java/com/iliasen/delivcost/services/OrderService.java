package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.*;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.OrderRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CargoService cargoService;

    private final OrderRepository orderRepository;
    private final PartnerRepository partnerRepository;
    private final ClientRepository clientRepository;
    public ResponseEntity<?> addOrder(Order order, Cargo cargo, Integer partnerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                Client client = clientRepository.findByEmail(userDetails.getUsername()).orElse(null);
                Partner partner = partnerRepository.findById(partnerId).orElse(null);

                if(partner!=null){
                    order.setCargo(cargo);
                    order.setClient(client);
                    order.setPartner(partner);
                    cargoService.addCargo(cargo, order);
                    orderRepository.save(order);
                }
            }
        }
        return ResponseEntity.badRequest().body("No necessary data");
    }

    public ResponseEntity<?> getOrders(String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PARTNER"))) {
                    Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                            .orElseThrow(() -> new NoSuchElementException("Partner not found"));

                    List<Order> orders = orderRepository.findByPartnerId(partner.getId());

                    if (status != null) {
                        OrderStatus filterStatus = OrderStatus.valueOf(status); // Преобразуем значение параметра в OrderStatus
                        orders = orders.stream()
                                .filter(order -> order.getOrderStatus() == filterStatus)
                                .collect(Collectors.toList());
                    }

                    // Сортировка заказов, чтобы завершенные заказы были в конце
                    Collections.sort(orders, Comparator.comparing(Order::getOrderStatus, Comparator.comparing(OrderStatus::ordinal)));

                    return ResponseEntity.ok(orders);
                } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
                    Client client = clientRepository.findByEmail(userDetails.getUsername())
                            .orElseThrow(() -> new NoSuchElementException("Client not found"));

                    List<Order> orders = orderRepository.findByClientId(client.getId());

                    if (status != null) {
                        OrderStatus filterStatus = OrderStatus.valueOf(status);
                        orders = orders.stream()
                                .filter(order -> order.getOrderStatus() == filterStatus)
                                .collect(Collectors.toList());
                    }

                    Collections.sort(orders, Comparator.comparing(Order::getOrderStatus, Comparator.comparing(OrderStatus::ordinal)));

                    return ResponseEntity.ok(orders);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

//    public ResponseEntity<?> getOrders(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.isAuthenticated()) {
//            Object principal = authentication.getPrincipal();
//
//            if (principal instanceof UserDetails) {
//                UserDetails userDetails = (UserDetails) principal;
//                if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PARTNER"))) {
//                    Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
//                            .orElseThrow(() -> new NoSuchElementException("Partner not found"));
//                    return ResponseEntity.ok(orderRepository.findByPartnerId(partner.getId()));
//                } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
//                    Client client = clientRepository.findByEmail(userDetails.getUsername())
//                            .orElseThrow(() -> new NoSuchElementException("Сlient not found"));
//                    return ResponseEntity.ok(orderRepository.findByClientId(client.getId()));
//                }
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }

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
