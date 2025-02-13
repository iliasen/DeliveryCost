package com.iliasen.delivcost.mapper;

import com.iliasen.delivcost.dto.DriverDTO;
import com.iliasen.delivcost.models.Driver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    DriverDTO toDriverDTO(Driver driver);
    Driver toDriver(DriverDTO driverDTO);
}
