package com.iliasen.delivcost.mapper;

import com.iliasen.delivcost.dto.WarehouseDTO;
import com.iliasen.delivcost.models.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    Warehouse toEntity(WarehouseDTO warehouseDTO);

    WarehouseDTO toWarehouseDTO(Warehouse warehouse);
}