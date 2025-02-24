package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Transport;
import com.iliasen.delivcost.models.TransportType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long>, JpaSpecificationExecutor<Transport> {
    List<Transport> findByPartnerId(Long id);

    List<Transport> findByPartnerIdAndTransportType(Long id, TransportType type);

    Page<Transport> findByPartnerId(Long id, Pageable of);
}
