package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    List<Notification> findByClientId(Long id);
    List<Notification> findByPartnerId(Long id);

    List<Notification> findByOrderId(Long orderId);

    void deleteByPartnerId(Long id);

    void deleteByClientId(Long id);
}
