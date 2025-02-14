package com.iliasen.delivcost.dto;

import lombok.Data;

@Data
public class CargoDTO {
    private Long id;
    private double weight;
    private double length;
    private double width;
    private double height;
}
