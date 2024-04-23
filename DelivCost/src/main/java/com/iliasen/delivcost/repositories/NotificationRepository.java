package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Notification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends CrudRepository<Notification, Integer> {
    List<Notification> findByClientId(Integer id);
    List<Notification> findByPartnerId(Integer id);

    List<Notification> findByOrderId(Integer orderId);

    void deleteByPartnerId(Integer id);

    void deleteByClientId(Integer id);
}
