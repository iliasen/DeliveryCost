package com.iliasen.delivcost.mapper;

import com.iliasen.delivcost.dto.RatingDTO;
import com.iliasen.delivcost.models.Rating;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RatingMapper {
    @Mapping(source = "partnerId", target = "partner.id")
    @Mapping(source = "clientId", target = "client.id")
    Rating toRating(RatingDTO ratingDTO);

    @InheritInverseConfiguration(name = "toEntity")
    RatingDTO toRatingDTO(Rating rating);
}