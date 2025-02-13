package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Cargo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CargoRepository extends CrudRepository<Cargo, Long> {
    Optional<Cargo> findByOrderId(Long id);
}
