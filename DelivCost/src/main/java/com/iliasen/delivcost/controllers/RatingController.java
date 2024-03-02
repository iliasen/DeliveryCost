package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.models.Rating;
import com.iliasen.delivcost.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rating")
public class RatingController {
    private final RatingService ratingService;

    @PostMapping(value = "/{id}")
    public ResponseEntity<?> addRating(@PathVariable Integer id, @RequestBody Rating req, @AuthenticationPrincipal UserDetails userDetails){
        return ratingService.createRating(id, req, userDetails);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPartner(@PathVariable Integer id){
        return ratingService.getById(id);
    }

    @GetMapping(value = "/average/{id}")
    public ResponseEntity<?> getAverage(@PathVariable Integer id){
        return ratingService.getAverageRating(id);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable Integer id, @AuthenticationPrincipal UserDetails userDetails){
        return ratingService.deleteRatingById(id, userDetails);
    }
}
