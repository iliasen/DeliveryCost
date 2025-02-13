package com.iliasen.delivcost.mapper;

import com.iliasen.delivcost.dto.OrderDTO;
import com.iliasen.delivcost.models.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(source = "order.orderStatus", target = "orderStatus")
    OrderDTO toOrderDTO(Order order);
    Order toOrder(OrderDTO orderDTO);
}