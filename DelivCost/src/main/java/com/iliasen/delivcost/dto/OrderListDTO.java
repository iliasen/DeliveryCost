package com.iliasen.delivcost.dto;

import com.iliasen.delivcost.models.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDTO {
    private List<Order> orderList;
}
