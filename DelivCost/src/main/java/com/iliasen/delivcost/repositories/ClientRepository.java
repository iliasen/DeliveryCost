package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    boolean existsByEmail(String email);
}