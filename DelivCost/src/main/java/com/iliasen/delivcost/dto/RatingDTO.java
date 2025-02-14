package com.iliasen.delivcost.dto;

import lombok.Data;

@Data
public class RatingDTO {
    private Long id;
    private Integer rate;
    private String feedback;
    private Long clientId;
    private Long partnerId;
}
