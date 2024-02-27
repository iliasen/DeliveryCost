package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Cargo;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CargoRepository extends CrudRepository<Cargo, Integer> {
    Optional<Cargo> findByOrderId(Integer id);
}
