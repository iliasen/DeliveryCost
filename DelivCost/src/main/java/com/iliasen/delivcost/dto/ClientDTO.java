package com.iliasen.delivcost.dto;

import lombok.Value;

/**
 * DTO for {@link com.iliasen.delivcost.models.Client}
 */
@Value
public class ClientDTO {
    Long id;
    String email;
    String phone;
    String firstName;
    String lastName;
}