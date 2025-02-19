package com.iliasen.delivcost.dto.mapper;

import com.iliasen.delivcost.dto.DriverDTO;
import com.iliasen.delivcost.models.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface DriverMapper {
    Driver toEntity(DriverDTO driverDTO);

    DriverDTO toDriverDTO(Driver driver);
}