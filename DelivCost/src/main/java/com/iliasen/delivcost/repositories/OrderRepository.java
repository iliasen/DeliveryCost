package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Order;
import com.iliasen.delivcost.models.Partner;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> {
    Page<Order> findByPartnerId(Long id, Pageable pageRequest);

    Page<Order> findByClientId(Long id, Pageable pageRequest);

    Page<Order> findByDriverId(Long id, Pageable pageRequest);

    List<Order> findByPartnerAndPartnerChecked(Partner partner, boolean b);

    List<Order> findByPartnerId(Long id);
}
