package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Driver;
import com.iliasen.delivcost.models.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Driver> findDriversByPartner(Partner partner);
}
