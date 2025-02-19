package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner,Long> {
    Optional<Partner> findByEmail(String email);
}