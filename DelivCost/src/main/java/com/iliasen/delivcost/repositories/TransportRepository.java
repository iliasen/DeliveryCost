package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.models.TransportType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransportRepository extends CrudRepository<Transport, Integer> {
    List<Transport> findByPartnerId(Integer id);

    List<Transport> findByPartnerIdAndTransportType(Integer id, TransportType type);
}
