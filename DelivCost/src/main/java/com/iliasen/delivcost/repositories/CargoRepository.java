package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargo, Integer> {
}
