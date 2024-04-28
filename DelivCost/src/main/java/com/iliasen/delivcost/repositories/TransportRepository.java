package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Transport;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransportRepository extends CrudRepository<Transport, Integer> {
    List<Transport> findByPartnerId(Integer id);
}
