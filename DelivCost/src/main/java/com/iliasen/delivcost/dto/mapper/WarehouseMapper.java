package com.iliasen.delivcost.dto.mapper;

import com.iliasen.delivcost.dto.WarehouseDTO;
import com.iliasen.delivcost.models.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface WarehouseMapper {
    Warehouse toEntity(WarehouseDTO warehouseDTO);

    WarehouseDTO toWarehouseDTO(Warehouse warehouse);
}