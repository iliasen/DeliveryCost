package com.iliasen.delivcost.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Route {
    private String pointOfDeparture;
    private String  deliveryPoint;
}
