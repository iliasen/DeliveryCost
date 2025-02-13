package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Partner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends CrudRepository<Partner,Long> {
    Optional<Partner> findByEmail(String email);
}