package com.iliasen.delivcost.dto.mapper;

import com.iliasen.delivcost.dto.PartnerDTO;
import com.iliasen.delivcost.models.Partner;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface PartnerMapper {
    Partner toEntity(PartnerDTO partnerDTO);

    PartnerDTO toPartnerDTO(Partner partner);

}