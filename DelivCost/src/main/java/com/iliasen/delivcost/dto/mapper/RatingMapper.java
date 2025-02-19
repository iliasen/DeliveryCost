package com.iliasen.delivcost.dto.mapper;

import com.iliasen.delivcost.dto.RatingDTO;
import com.iliasen.delivcost.models.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface RatingMapper {
    Rating toEntity(RatingDTO ratingDTO);

    RatingDTO toRatingDTO(Rating rating);
}