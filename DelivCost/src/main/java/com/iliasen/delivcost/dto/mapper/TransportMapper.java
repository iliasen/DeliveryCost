package com.iliasen.delivcost.dto.mapper;

import com.iliasen.delivcost.dto.TransportDTO;
import com.iliasen.delivcost.models.Transport;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface TransportMapper {
    Transport toEntity(TransportDTO transportDTO);

    TransportDTO toTransportDTO(Transport transport);
}