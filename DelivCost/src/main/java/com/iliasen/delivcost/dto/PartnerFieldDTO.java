package com.iliasen.delivcost.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartnerFieldDTO {
    private String companyOfficial;
    private String description;
    private int margin;
}
