package com.iliasen.delivcost.dto;

import com.iliasen.delivcost.models.TransportType;
import lombok.Data;
import lombok.Value;

@Data
public class TransportDTO {
    private Long id;
    private TransportType transportType;
    private double tonnage;
    private double volume;
    private DriverDTO driver;
}