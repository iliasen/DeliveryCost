package com.iliasen.delivcost.mapper;

import com.iliasen.delivcost.dto.PartnerDTO;
import com.iliasen.delivcost.models.Partner;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PartnerMapper {
    Partner toEntity(PartnerDTO partnerDTO);

    PartnerDTO toPartnerDTO(Partner partner);
}