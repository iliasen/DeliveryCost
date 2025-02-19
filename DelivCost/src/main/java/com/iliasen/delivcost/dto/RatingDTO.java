package com.iliasen.delivcost.dto;

import lombok.Value;

/**
 * DTO for {@link com.iliasen.delivcost.models.Rating}
 */
@Value
public class RatingDTO {
    Long id;
    Integer rate;
    String feedback;
    ClientDTO client;
    PartnerDTO partner;
}