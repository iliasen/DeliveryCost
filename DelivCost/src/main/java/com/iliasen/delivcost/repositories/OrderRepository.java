package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Order;
import com.iliasen.delivcost.models.Partner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {
    List<Order> findByPartnerId(Long id);

    List<Order> findByClientId(Long id);

    List<Order> findByDriverId(Long id);

    List<Order> findByPartnerAndPartnerChecked(Partner partner, boolean b);

}
