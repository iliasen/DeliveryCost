package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {
    Page<Notification> findByClientId(Long id, Pageable pageable);
    Page<Notification> findByPartnerId(Long id, Pageable pageable);

    List<Notification> findByOrderId(Long orderId);

    void deleteByPartnerId(Long id);

    void deleteByClientId(Long id);
}
