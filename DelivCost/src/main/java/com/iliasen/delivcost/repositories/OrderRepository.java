package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Order;
import com.iliasen.delivcost.models.Partner;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findByPartnerId(Integer id);

    List<Order> findByClientId(Integer id);

    List<Order> findByDriverId(Integer id);

    List<Order> findByPartnerAndPartnerChecked(Partner partner, boolean b);

}
