package com.iliasen.delivcost.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iliasen.delivcost.models.*;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.DriverRepository;
import com.iliasen.delivcost.repositories.OrderRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final CargoService cargoService;
    private final NotificationService notificationService;

    private final OrderRepository orderRepository;
    private final PartnerRepository partnerRepository;
    private final ClientRepository clientRepository;
    private final DriverRepository driverRepository;
    private final TransportService transportService;


    public ResponseEntity<?> addOrder(Order order, Cargo cargo, Integer partnerId, UserDetails userDetails) {
        try {
            Client client = clientRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
            Partner partner = partnerRepository.findById(partnerId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

            order.setClient(client);
            order.setPartner(partner);
            order.setCargo(cargo);
            orderRepository.save(order);

            cargoService.addCargo(cargo, order);

            return ResponseEntity.ok("Order added");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error adding order", e);
        }
    }

    public ResponseEntity<?> transferOrdersToTheDriver(Integer driverId,List<Order> orders) {
        Driver driver = driverRepository.findById(driverId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));

        Transport transport = driver.getTransport();

        if (transportService.calculateVolume(transport, orders)) {
            // Добавляем каждый заказ по отдельности
            for (Order order : orders) {
                order.setDriver(driver);
                orderRepository.save(order);
                driver.getOrders().add(order);
            }
            driverRepository.save(driver);
            return ResponseEntity.ok("Orders transferred");
        } else {
            return ResponseEntity.badRequest().body("You have exceeded the load capacity of the vehicle");
        }
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
        } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("DRIVER"))) {
            Driver driver = driverRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Driver not found"));

            orders = orderRepository.findByDriverId(driver.getId());
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
        }else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    public ResponseEntity<?> getOrdersForPartner(UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Partner not found"));
        List<Order> orders = orderRepository.findByPartnerId(partner.getId());

        orders = orders.stream()
                .filter(order -> order.getOrderStatus() != OrderStatus.COMPLETE)
                .collect(Collectors.toList());
        Collections.sort(orders, Comparator.comparing(
                Order::getOrderStatus,
                Comparator.nullsLast(Comparator.comparing(OrderStatus::ordinal))
        ));
        if (!orders.isEmpty()) {
            return ResponseEntity.ok(orders);
        }else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    public ResponseEntity<List<Order>> getOrdersForDriver(Integer id, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));
        Driver driver = driverRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));

        List<Order> orders = orderRepository.findByPartnerId(partner.getId());
        orders = orders.stream()
                .filter(order -> order.getRoute().getTransportType() == driver.getTransport().getTransportType() && order.getOrderStatus() != OrderStatus.COMPLETE && order.getDriver() == null)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    public ResponseEntity<?> getOrdersByTransportType(String type, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(type);

            String transportTypeString = jsonNode.path("type").asText();

            TransportType transportType = TransportType.valueOf(transportTypeString);
            List<Order> orders = orderRepository.findByPartnerId(partner.getId());
            orders = orders.stream()
                    .filter(order -> order.getRoute().getTransportType() == transportType)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getNewOrders(UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Partner not found"));
        List<Order> orders = orderRepository.findByPartnerAndPartnerChecked(partner, false);
        return ResponseEntity.ok(orders);
    }

    public ResponseEntity<?> getOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return ResponseEntity.ok(order);
    }

    public ResponseEntity<?> updateStatus(Integer id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.COMPLETE) {
            cargoService.transferCargoToWarehouse(order);
        }

        order.setOrderStatus(newStatus);
        orderRepository.save(order);

        notificationService.createNotify(order);

        return ResponseEntity.ok("Status update");
    }

    public ResponseEntity<?> setPartnerView(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        order.setPartnerChecked(true);
        orderRepository.save(order);
        return ResponseEntity.ok("Partner checked order");
    }

    public ResponseEntity<?> backpackProblemSolver(int maxWeight, UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Partner not found"));

        List<Order> orders = orderRepository.findByPartnerId(partner.getId());

        List<Order> selectedOrders = new ArrayList<>();

        int[][] dp = new int[orders.size() + 1][maxWeight + 1];

        for (int i = 1; i <= orders.size(); i++) {
            Order order = orders.get(i - 1);
            double volume = order.getCargo().getVolume();
            int price = order.getPrice();

            for (int j = 1; j <= maxWeight; j++) {
                if (volume <= j) {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][(int) (j - volume)] + price);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        int remainingWeight = maxWeight;
        for (int i = orders.size(); i > 0; i--) {
            if (dp[i][remainingWeight] != dp[i - 1][remainingWeight]) {
                Order selectedOrder = orders.get(i - 1);
                selectedOrders.add(selectedOrder);
                remainingWeight -= selectedOrder.getCargo().getWeight();
            }
        }

        return new ResponseEntity<>(selectedOrders, HttpStatus.OK);
    }

}
