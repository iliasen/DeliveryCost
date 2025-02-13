package com.iliasen.delivcost.repositories;

import com.iliasen.delivcost.models.Rating;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long> {

    Iterable<Rating> findByPartnerId(Long id);

    Rating findByClientIdAndPartnerId(Long id, Long id1);
}