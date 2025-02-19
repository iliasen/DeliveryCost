package com.iliasen.delivcost.dto.mapper;

import com.iliasen.delivcost.dto.CargoDTO;
import com.iliasen.delivcost.models.Cargo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CargoMapper {
    Cargo toEntity(CargoDTO cargoDTO);

    CargoDTO toCargoDTO(Cargo cargo);
}