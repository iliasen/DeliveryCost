package com.iliasen.delivcost.services;

import com.iliasen.delivcost.dto.RatingDTO;
import com.iliasen.delivcost.dto.mapper.RatingMapper;
import com.iliasen.delivcost.models.Client;
import com.iliasen.delivcost.models.Partner;
import com.iliasen.delivcost.models.Rating;
import com.iliasen.delivcost.repositories.ClientRepository;
import com.iliasen.delivcost.repositories.PartnerRepository;
import com.iliasen.delivcost.repositories.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final ClientRepository clientRepository;
    private final PartnerRepository partnerRepository;
    private final RatingMapper ratingMapper;

    public RatingDTO createRating(Long partnerId, Rating req, UserDetails userDetails) {

        Client client = clientRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        Rating rating = ratingRepository.findByClientIdAndPartnerId(client.getId(), partner.getId());

        if (rating != null) {//if new
            rating.setRate(req.getRate());
            rating.setFeedback(req.getFeedback());
            ratingRepository.save(rating);
            return ratingMapper.toRatingDTO(rating);
        } else {
            rating = new Rating();
            rating.setRate(req.getRate());
            rating.setFeedback(req.getFeedback());
            rating.setClient(client);
            rating.setPartner(partner);
            ratingRepository.save(rating);
            return ratingMapper.toRatingDTO(rating);
        }
    }


    public List<RatingDTO>  getById(Long partnerId) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        Iterable<Rating> ratings = ratingRepository.findByPartnerId(partnerId);
        List<RatingDTO> ratingsDTO = StreamSupport.stream(ratings.spliterator(), false)
                .map(ratingMapper::toRatingDTO)
                .collect(Collectors.toList());
        return ratingsDTO;
    }



    public double getAverageRating(Long partnerId) {
        partnerRepository.findById(partnerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Partner not found"));

        Iterable<Rating> ratings = ratingRepository.findByPartnerId(partnerId);
        int count = 0;
        int sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRate();
            count++;
        }

        if (count > 0) {
            return (double) sum / count;
        } else {
            throw new NoSuchElementException();
        }
    }

    public String deleteRatingById(Long partnerId, UserDetails userDetails) {

        Client client = clientRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        Rating rating = ratingRepository.findByClientIdAndPartnerId(client.getId(), partnerId);
        if (rating == null) {
            throw new NoSuchElementException();
        }
        ratingRepository.deleteById(rating.getId());
        return "Rating wad delete";
    }

}