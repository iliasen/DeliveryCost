package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Warehouse;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface WarehouseRepository extends CrudRepository<Warehouse, Integer> {
    Optional<Warehouse> findByClientId(Integer id);
}
