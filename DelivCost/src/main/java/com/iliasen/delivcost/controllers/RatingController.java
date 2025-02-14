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
    public ResponseEntity<?> addRating(@PathVariable Long id, @RequestBody Rating req, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(ratingService.createRating(id, req, userDetails));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getPartner(@PathVariable Long id){
        return ResponseEntity.ok(ratingService.getById(id));
    }

    @GetMapping(value = "/average/{id}")
    public ResponseEntity<Double> getAverage(@PathVariable Long id){
        return ResponseEntity.ok(ratingService.getAverageRating(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteRating(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.ok(ratingService.deleteRatingById(id, userDetails));
    }
}
