package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.*;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.NotificationRepository;
import com.iliasen.delivcost.repositories.OrderRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final ClientRepository clientRepository;
    private final PartnerRepository partnerRepository;
    private final OrderRepository orderRepository;

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

    public ResponseEntity<?> viewNotify(Integer id, UserDetails userDetails) {
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
        return ResponseEntity.ok("Checked status update");
    }

    public ResponseEntity<?> getAllNotifications(UserDetails userDetails){
        List<Notification> notifications = new ArrayList<>();
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PARTNER"))) {
            Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Partner not found"));

            notifications = notificationRepository.findByPartnerId(partner.getId());
        } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
            Client client = clientRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Client not found"));

            notifications = notificationRepository.findByClientId(client.getId());
        }
        return ResponseEntity.ok(notifications);
    }

    public ResponseEntity<?> changeSubscribe(Integer orderId,boolean subscribe, UserDetails userDetails){
        Client client = clientRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new NoSuchElementException("Client not found"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
        if(order.getClient()==client){
            order.setClientSubscribe(subscribe);
            orderRepository.save(order);
            return ResponseEntity.ok("Subscribe changes");
        }
        return ResponseEntity.notFound().build();
    }

    @Transactional
    public ResponseEntity<?> deleteAllNotifications(UserDetails userDetails) {
        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("PARTNER"))) {
            Partner partner = partnerRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Partner not found"));

            notificationRepository.deleteByPartnerId(partner.getId());
        } else if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("CLIENT"))) {
            Client client = clientRepository.findByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new NoSuchElementException("Client not found"));

            notificationRepository.deleteByClientId(client.getId());
        }

        return ResponseEntity.ok("All notifications have been deleted.");
    }
}
