package com.iliasen.delivcost.services;

import com.iliasen.delivcost.dto.NotificationDTO;
import com.iliasen.delivcost.dto.mapper.NotificationMapper;
import com.iliasen.delivcost.models.*;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.NotificationRepository;
import com.iliasen.delivcost.repositories.OrderRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.specification.NotificationSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ClientRepository clientRepository;
    private final PartnerRepository partnerRepository;
    private final OrderRepository orderRepository;
    private final NotificationMapper notificationMapper;

    public void createNotify(Order order) {

        List<Notification> notifications = new ArrayList<>();

        List<Notification> existingNotification = notificationRepository.findByOrderId(order.getId());
        System.out.println(existingNotification);
        if (!existingNotification.isEmpty()) {
            System.out.println(order.isClientSubscribe());
           if (order.isClientSubscribe()) {
               
                // Если подписка клиента активна, создаем новое уведомление
                Notification newNotification = Notification.builder()
                        .newStatus(order.getOrderStatus())
                        .client(order.getClient())
                        .partner(order.getPartner())
                        .order(order)
                        .changeTime(LocalDateTime.now())
                        .build();

                notifications.add(newNotification);
            }
        } else {
            // Если уведомление для данного заказа не существует, создаем новое уведомление
            Notification notification = Notification.builder()
                    .newStatus(order.getOrderStatus())
                    .client(order.getClient())
                    .partner(order.getPartner())
                    .order(order)
                    .changeTime(LocalDateTime.now())
                    .build();

            notifications.add(notification);
        }

        notificationRepository.saveAll(notifications);
    }

    public String viewNotify(Long id, UserDetails userDetails) {
        Notification notification;
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PARTNER"))) {
            partnerRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Partner not found"));

            notification = notificationRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Notification not found"));
            notification.setPartnerChecked(true);
            notificationRepository.save(notification);
        } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
            clientRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Client not found"));
            notification = notificationRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Notification not found"));
            notification.setClientChecked(true);
            notificationRepository.save(notification);

        }
        return "Checked status update";
    }

    public Page<NotificationDTO> getAllNotifications(Pageable pageable, UserDetails userDetails) {
        Page<Notification> notifications;

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PARTNER"))) {
            Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Partner not found"));

            notifications = notificationRepository.findByPartnerId(partner.getId(), pageable);
        } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
            Client client = clientRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Client not found"));

            notifications = notificationRepository.findByClientId(client.getId(), pageable);
        } else {
            throw new IllegalArgumentException("Invalid user");
        }

        return notifications.map(notificationMapper::toNotificationDTO);
    }


    public String changeSubscribe(Long orderId, boolean subscribe, UserDetails userDetails) {
        Client client = clientRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        if (order.getClient() == client) {
            order.setClientSubscribe(subscribe);
            orderRepository.save(order);
            return "Subscribe changes";
        }
        throw new NoSuchElementException("Order not found");
    }


    @Transactional
    public String deleteAllNotifications(UserDetails userDetails) {
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PARTNER"))) {
            Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Partner not found"));

            notificationRepository.deleteByPartnerId(partner.getId());
        } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
            Client client = clientRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Client not found"));

            notificationRepository.deleteByClientId(client.getId());
        }

        return "All notifications have been deleted.";
    }


    public List<Notification> filterAndSortNotifications(OrderStatus newStatus, boolean partnerChecked, boolean clientChecked, LocalDateTime changeTime, Order order, Partner partner, Client client, boolean sortByChangeTimeAsc) {
        Specification<Notification> spec = Specification
                .where(NotificationSpecification.hasNewStatus(newStatus))
                .and(NotificationSpecification.isPartnerChecked(partnerChecked))
                .and(NotificationSpecification.isClientChecked(clientChecked))
                .and(NotificationSpecification.hasChangeTimeAfter(changeTime))
                .and(NotificationSpecification.hasOrder(order))
                .and(NotificationSpecification.hasPartner(partner))
                .and(NotificationSpecification.hasClient(client));

        if (sortByChangeTimeAsc) {
            spec = spec.and(NotificationSpecification.orderByChangeTime(true));
        } else {
            spec = spec.and(NotificationSpecification.orderByChangeTime(false));
        }

        return notificationRepository.findAll(spec);
    }
}
