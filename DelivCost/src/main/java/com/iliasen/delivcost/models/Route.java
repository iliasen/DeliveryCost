package com.iliasen.delivcost.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Route {
    private String pointOfDeparture;
    private String  deliveryPoint;
    private int distance;
    private LocalDateTime deliveryTime;
    private TransportType transportType;
}
