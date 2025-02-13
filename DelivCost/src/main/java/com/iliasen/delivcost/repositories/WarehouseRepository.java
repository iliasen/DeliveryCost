package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Warehouse;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WarehouseRepository extends CrudRepository<Warehouse, Long> {
    Optional<Warehouse> findByClientId(Long id);

    Optional<Warehouse> findByClientEmail(String username);
}
