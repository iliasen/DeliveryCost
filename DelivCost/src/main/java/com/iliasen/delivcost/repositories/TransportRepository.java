package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.models.TransportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends CrudRepository<Transport, Long> {
    List<Transport> findByPartnerId(Long id);

    List<Transport> findByPartnerIdAndTransportType(Long id, TransportType type);

    Page<Transport> findByPartnerId(Long id, Pageable of);
}
