package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Partner;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PartnerRepository extends CrudRepository<Partner,Integer> {
    Optional<Partner> findByEmail(String email);
}