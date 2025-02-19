package com.iliasen.delivcost.dto.mapper;

import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.models.Cargo;
import com.iliasen.delivcost.models.Order;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OrderMapper {
    Order toEntity(OrderDTO orderDTO);

    @AfterMapping
    default void linkCargo(@MappingTarget Order order) {
        Cargo cargo = order.getCargo();
        if (cargo != null) {
            cargo.setOrder(order);
        }
    }

    OrderDTO toOrderDTO(Order order);
}