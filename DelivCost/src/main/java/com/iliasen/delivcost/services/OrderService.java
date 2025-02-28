package com.iliasen.delivcost.services;

import com.iliasen.delivcost.dto.OrderAndCargoRequest;
import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.exeptions.DriverNotFoundException;
import com.iliasen.delivcost.exeptions.OrderNotFoundException;
import com.iliasen.delivcost.exeptions.TransportOverloadedException;
import com.iliasen.delivcost.dto.mapper.CargoMapper;
import com.iliasen.delivcost.dto.mapper.OrderMapper;
import com.iliasen.delivcost.models.*;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.DriverRepository;
import com.iliasen.delivcost.repositories.OrderRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
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
    private final OrderMapper orderMapper;
    private final CargoMapper cargoMapper;

    public OrderDTO addOrder(OrderAndCargoRequest request, Long partnerId, UserDetails userDetails) {
        Client client = clientRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        Order order = orderMapper.toEntity(request.order());
        Cargo cargo = cargoMapper.toEntity(request.cargo());

        order.setClient(client);
        order.setPartner(partner);
        order.setCargo(cargo);
        orderRepository.save(order);

        cargoService.addCargo(cargo, order);

        return orderMapper.toOrderDTO(order);
    }

    public String transferOrdersToTheDriver(Long driverId, List<OrderDTO> orderList) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new DriverNotFoundException("Driver not found"));


        List<Order> orders = orderList.stream()
                .map(orderMapper::toEntity)
                .collect(Collectors.toList());

        Transport transport = driver.getTransport();
        List<Order> foundOrders = findOrdersByIds(orders);

        if (transportService.calculateVolume(transport, foundOrders)) {
            transferOrdersToDriver(driver, foundOrders);
            return "Orders transferred";
        } else {
            throw new TransportOverloadedException("You have exceeded the load capacity of the vehicle");
        }
    }

    private List<Order> findOrdersByIds(List<Order> orders) {
        List<Order> foundOrders = new ArrayList<>();
        for (Order order : orders) {
            orderRepository.findById(order.getId()).ifPresent(foundOrders::add);
        }
        if (foundOrders.isEmpty()) {
            throw new OrderNotFoundException("No valid orders found in the provided list");
        }
        return foundOrders;
    }

    private void transferOrdersToDriver(Driver driver, List<Order> orders) {
        for (Order order : orders) {
            order.setDriver(driver);
            orderRepository.save(order);
            driver.getOrders().add(order);
        }
        driverRepository.save(driver);
    }


    public Page<OrderDTO> getOrders(
            Pageable pageable,
            String status,
            UserDetails userDetails,
            Long driverId,
            Long partnerId,
            Long clientId,
            boolean forTransfer) {

        try {
            Specification<Order> spec = Specification.where(null);

            // Парсинг статуса
            OrderStatus filterStatus = null;
            if (status != null) {
                try {
                    filterStatus = OrderStatus.valueOf(status);
                } catch (IllegalArgumentException e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status value");
                }
                spec = spec.and(OrderSpecification.hasStatus(filterStatus));
            }

            // Фильтрация по пользователю
            if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PARTNER"))) {
                Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));
                spec = spec.and(OrderSpecification.belongsToPartner(partner.getId()));
            } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
                Client client = clientRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
                spec = spec.and(OrderSpecification.belongsToClient(client.getId()));
            } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("DRIVER"))) {
                Driver driver = driverRepository.findByEmail(userDetails.getUsername())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
                spec = spec.and(OrderSpecification.assignedToDriver(driver.getId()));
            } else {
                throw new IllegalArgumentException("Invalid authority");
            }

            // Дополнительная фильтрация по параметрам
            if (driverId != null) {
                spec = spec.and(OrderSpecification.assignedToDriver(driverId));
            }

            if (partnerId != null) {
                spec = spec.and(OrderSpecification.belongsToPartner(partnerId));
            }

            if (clientId != null) {
                spec = spec.and(OrderSpecification.belongsToClient(clientId));
            }

            if (forTransfer) {
                spec = spec.and(OrderSpecification.hasNoDriver())
                        .and(OrderSpecification.isNotComplete());

                // Если нужно фильтровать по типу транспорта драйвера
                if (driverId != null) {
                    Driver driver = driverRepository.findById(driverId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Driver not found"));
                    spec = spec.and(OrderSpecification.hasTransportType(driver.getTransport().getTransportType()));
                }
            }


            Page<Order> orders = orderRepository.findAll(spec, pageable);

            List<Order> sortedOrders = orders.getContent().stream()
                    .sorted(Comparator.comparing(
                            Order::getOrderStatus,
                            Comparator.nullsLast(Comparator.comparing(OrderStatus::ordinal))
                    ))
                    .collect(Collectors.toList());

            List<OrderDTO> orderDTOs = sortedOrders.stream()
                    .map(orderMapper::toOrderDTO)
                    .collect(Collectors.toList());

            return new PageImpl<>(orderDTOs, pageable, orders.getTotalElements());

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }



    public List<OrderDTO> getNewOrders(UserDetails userDetails) {
        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Partner not found"));

        return orderRepository.findByPartnerAndPartnerChecked(partner, false).stream().map(orderMapper::toOrderDTO).toList();
    }

    public OrderDTO getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return orderMapper.toOrderDTO(order);
    }

    public String updateStatus(Long id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.COMPLETE) {
            cargoService.transferCargoToWarehouse(order);
        }

        order.setOrderStatus(newStatus);
        orderRepository.save(order);

        notificationService.createNotify(order);

        return "Status update";
    }

    public String setPartnerView(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        order.setPartnerChecked(true);
        orderRepository.save(order);
        return "Partner checked order";
    }


    //нужно переделать
//    public ResponseEntity<?> backpackProblemSolver(int maxWeight, UserDetails userDetails) {
//        Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new NoSuchElementException("Partner not found"));
//
//        List<Order> orders = orderRepository.findByPartnerId(partner.getId());
//
//        List<Order> selectedOrders = new ArrayList<>();
//
//        int[][] dp = new int[orders.size() + 1][maxWeight + 1];
//
//        for (int i = 1; i <= orders.size(); i++) {
//            Order order = orders.get(i - 1);
//            double volume = order.getCargo().getVolume();
//            int price = order.getPrice();
//
//            for (int j = 1; j <= maxWeight; j++) {
//                if (volume <= j) {
//                    dp[i][j] = Math.max(dp[i - 1][j], dp[i - 1][(int) (j - volume)] + price);
//                } else {
//                    dp[i][j] = dp[i - 1][j];
//                }
//            }
//        }
//
//        int remainingWeight = maxWeight;
//        for (int i = orders.size(); i > 0; i--) {
//            if (dp[i][remainingWeight] != dp[i - 1][remainingWeight]) {
//                Order selectedOrder = orders.get(i - 1);
//                selectedOrders.add(selectedOrder);
//                remainingWeight -= selectedOrder.getCargo().getWeight();
//            }
//        }
//
//        return new ResponseEntity<>(selectedOrders, HttpStatus.OK);
//    }

}
