package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Driver;
import com.iliasen.delivcost.models.Partner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long>, JpaSpecificationExecutor<Driver> {
    Optional<Driver> findByEmail(String email);
    boolean existsByEmail(String email);
    Page<Driver> findDriversByPartner(Partner partner, Pageable pageRequest);

    Page<Driver> findDriversByPartnerAndTransportIsNull(Partner partner, PageRequest of);
}
