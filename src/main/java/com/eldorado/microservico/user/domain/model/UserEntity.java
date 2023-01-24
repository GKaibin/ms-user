package com.eldorado.microservico.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("user")
public class UserEntity {

    @NonNull
    private String name;

    private char gender;

    @NonNull
    private LocalDate birthDate;

    @NonNull
    @Id
    private String document;

    @NonNull
    private String email;

    private String password;

    private AddressEntity addressEntity;

    @Override
    public String toString() {
        return "UserEntity{" +
                "name='" + name + '\'' +
                ", gender=" + gender +
                ", birthDate=" + birthDate +
                ", document='" + document + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
