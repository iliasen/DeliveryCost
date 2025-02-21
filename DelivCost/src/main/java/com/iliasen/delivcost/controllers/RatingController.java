package com.iliasen.delivcost.controllers;

import com.iliasen.delivcost.models.Rating;
import com.iliasen.delivcost.services.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rating")
@Tag(name = "Ratings", description = "Interactions with ratings")
public class RatingController {
    private final RatingService ratingService;

    @Operation(summary = "Add rating", description = "Adds a rating for a specific entity")
    @PostMapping(value = "/{id}")
    public ResponseEntity<?> addRating(
            @PathVariable Long id,
            @RequestBody Rating req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ratingService.createRating(id, req, userDetails));
    }

    @Operation(summary = "Get rating by ID", description = "Returns a specific rating by its ID")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getRating(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getById(id));
    }

    @Operation(summary = "Get average rating", description = "Returns the average rating for a specific entity")
    @GetMapping(value = "/average/{id}")
    public ResponseEntity<Double> getAverage(@PathVariable Long id) {
        return ResponseEntity.ok(ratingService.getAverageRating(id));
    }

    @Operation(summary = "Delete rating", description = "Deletes a specific rating by its ID")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteRating(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ratingService.deleteRatingById(id, userDetails));
    }
}
