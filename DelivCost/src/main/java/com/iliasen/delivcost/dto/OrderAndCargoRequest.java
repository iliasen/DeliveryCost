package com.iliasen.delivcost.dto;

import com.iliasen.delivcost.models.Cargo;
import com.iliasen.delivcost.models.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderAndCargoRequest {
    private Order order;
    private Cargo cargo;
}
