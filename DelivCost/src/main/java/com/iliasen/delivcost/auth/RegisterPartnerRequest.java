package com.iliasen.delivcost.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPartnerRequest {
    private String companyName;
    private Long inn;
    private String contactNumber;
    private String email;
    private String password;
}
