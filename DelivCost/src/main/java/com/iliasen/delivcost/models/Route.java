package com.iliasen.delivcost.models;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
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
