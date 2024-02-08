package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Client;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Integer> {
    Optional<Client> findByEmail(String email);
}