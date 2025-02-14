package com.iliasen.delivcost.dto;

import lombok.Value;

/**
 * DTO for {@link com.iliasen.delivcost.models.Warehouse}
 */
@Value
public class WarehouseDTO {
    Long id;
    int length;
    int width;
    int height;
}