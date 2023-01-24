package com.eldorado.microservico.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddressDto {

    private String zipCode;

    private String street;

    private String number;

    private String city;

    private String state;

    private String country;
}
