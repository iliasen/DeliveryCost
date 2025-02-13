package com.iliasen.delivcost.mapper;

import com.iliasen.delivcost.dto.CargoDTO;
import com.iliasen.delivcost.models.Cargo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CargoMapper {
    CargoMapper INSTANCE = Mappers.getMapper(CargoMapper.class);

    @Mapping(expression = "java(cargo.getVolume())", target = "volume")
    CargoDTO toCargoDTO(Cargo cargo);
    Cargo toCargo(CargoDTO cargoDTO);
}