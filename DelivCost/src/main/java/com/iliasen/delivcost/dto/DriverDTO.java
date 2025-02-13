package com.iliasen.delivcost.dto;

import com.iliasen.delivcost.models.Role;
import lombok.Data;

@Data
public class DriverDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Role role;
}
