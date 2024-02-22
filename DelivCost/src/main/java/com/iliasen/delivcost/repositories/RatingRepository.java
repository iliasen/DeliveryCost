package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RatingRepository extends JpaRepository<Rating, Integer> {

    Iterable<Rating> findByPartnerId(Integer id);

    Rating findByClientIdAndPartnerId(Integer id, Integer id1);
}