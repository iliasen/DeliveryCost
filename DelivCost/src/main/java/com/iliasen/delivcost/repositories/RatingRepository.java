package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Rating;
import org.springframework.data.repository.CrudRepository;


public interface RatingRepository extends CrudRepository<Rating, Integer> {

    Iterable<Rating> findByPartnerId(Integer id);

    Rating findByClientIdAndPartnerId(Integer id, Integer id1);
}