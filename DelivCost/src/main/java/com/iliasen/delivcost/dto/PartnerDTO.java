package com.iliasen.delivcost.dto;

import com.iliasen.delivcost.models.Role;
import lombok.Value;

/**
 * DTO for {@link com.iliasen.delivcost.models.Partner}
 */
@Value
public class PartnerDTO {
    Long id;
    String companyName;
    Long inn;
    String email;
    String contactNumber;
    String companyOfficial;
    String description;
    int margin;
    String companyLogo;
    Role role;
}