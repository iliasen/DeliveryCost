package com.iliasen.delivcost.services;

import com.iliasen.delivcost.models.Client;
import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Rating;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.repositories.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ClientRepository clientRepository;
    private final PartnerRepository partnerRepository;

    public ResponseEntity<?> createRating(Integer partnerId,Rating req) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                Client client = clientRepository.findByEmail(userDetails.getUsername()).orElse(null);
                Partner partner = partnerRepository.findById(partnerId).orElse(null);

                Rating rating = ratingRepository.findByClientIdAndPartnerId(client.getId(), partner.getId());

                if (rating != null) {
                    rating.setRate(req.getRate());
                    rating.setFeedback(req.getFeedback());
                    ratingRepository.save(rating);
                    return ResponseEntity.ok(rating);
                } else {
                    rating = new Rating();
                    rating.setRate(req.getRate());
                    rating.setFeedback(req.getFeedback());
                    rating.setClient(client);
                    rating.setPartner(partner);
                    ratingRepository.save(rating);
                    return ResponseEntity.ok(rating);
                }
            }
        }
        return ResponseEntity.badRequest().body("Не найден отзыв");
    }

    public ResponseEntity<?> getById(Integer partnerId) {
        Partner partner = partnerRepository.findById(partnerId).orElse(null);

        if (partner == null) {
            return ResponseEntity.notFound().build();
        }
        Iterable<Rating> ratings = ratingRepository.findByPartnerId(partnerId);
        return ResponseEntity.ok(ratings);
    }


    public ResponseEntity<?> getAverageRating(Integer partnerId) {
        Partner partner = partnerRepository.findById(partnerId).orElse(null);
        if(partner == null){
            return  ResponseEntity.notFound().build();
        }
        Iterable<Rating> ratings = ratingRepository.findByPartnerId(partnerId);
        int count = 0;
        int sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRate();
            count++;
        }

        if (count > 0) {
            float average = (float) sum / count;
            return ResponseEntity.ok(average);
        } else {
            return ResponseEntity.ok(0f);
        }
    }

    public ResponseEntity<?> deleteRatingById(Integer partnerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                Client client = clientRepository.findByEmail(userDetails.getUsername()).orElse(null);

                Rating rating = ratingRepository.findByClientIdAndPartnerId(client.getId(), partnerId);
                if (rating == null) {
                    return ResponseEntity.notFound().build();
                }
                ratingRepository.deleteById(rating.getId());
                return ResponseEntity.ok("Рейтинг удалён");
            }
        }
        return ResponseEntity.badRequest().body("Не найден отзыв");
    }
}