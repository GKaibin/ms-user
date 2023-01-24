package com.eldorado.microservico.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressEntity {

    private String zipCode;

    private String street;

    private String number;

    private String city;

    private String state;

    private String country;
}
