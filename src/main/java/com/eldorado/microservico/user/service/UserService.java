package com.eldorado.microservico.user.service;

import com.eldorado.microservico.user.domain.model.AddressEntity;
import com.eldorado.microservico.user.domain.model.UserEntity;
import com.eldorado.microservico.user.dto.AddressDto;
import com.eldorado.microservico.user.dto.MessageDto;
import com.eldorado.microservico.user.dto.UserDto;
import com.eldorado.microservico.user.publiser.UserPublisher;
import com.eldorado.microservico.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private static final String MESSAGE = "Registration done\nUser: %s\nPassword: %s";

    private static final String SUBJECT = "DO NOT RESPOND";

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    private final UserPublisher userPublisher;

    private final ObjectMapper objectMapper;

    public UserDto createUser(@Validated final UserDto userDto) throws JsonProcessingException {

        log.info("UserDto: {}", userDto);

        var userEntity = modelMapper.map(userDto, UserEntity.class);

        log.info("UserEntity: {}", userEntity);

        final var addressEntity = modelMapper.map(userDto.getAddressDto(), AddressEntity.class);

        log.info("AddressEntity: {}", addressEntity);

        userEntity.setAddressEntity(addressEntity);

        log.info("UserEntity: {}", userEntity);

        final var password = passwordGenerator();

        userEntity.setPassword(Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString());

        userEntity = userRepository.save(userEntity);

        sendMessage(userDto, password);

        final var userDtoResponse = modelMapper.map(userEntity, UserDto.class);
        final var addressDtoResponse = modelMapper.map(userEntity.getAddressEntity(), AddressDto.class);
        userDtoResponse.setAddressDto(addressDtoResponse);

        return userDtoResponse;
    }

    private String passwordGenerator() {
        return Base64.encodeBase64String(RandomStringUtils.randomAlphanumeric(10).getBytes());
    }

    private void sendMessage(final UserDto userDto, final String password) throws JsonProcessingException {
        final var message = MessageDto.builder()
                .to(userDto.getEmail())
                .message(String.format(MESSAGE, userDto.getEmail(), password))
                .subject(SUBJECT)
                .build();

        userPublisher.sendToQueue(objectMapper.writeValueAsString(message));

        log.info("Message sent: {}", message);
    }
}
