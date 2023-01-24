package com.eldorado.microservico.user.service;

import com.eldorado.microservico.user.domain.model.UserEntity;
import com.eldorado.microservico.user.dto.MessageDto;
import com.eldorado.microservico.user.dto.UserDto;
import com.eldorado.microservico.user.repository.UserRepository;
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

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    private static final String MESSAGE = "Registration done\nUser: %s\nPassword: %s";

    private static final String SUBJECT = "DO NOT RESPOND";

    public UserDto createUser(@Validated final UserDto userDto) {

        var userEntity = modelMapper.map(userDto, UserEntity.class);

        final var password = passwordGenerator();

        userEntity.setPassword(Hashing.sha256()
                .hashString(password, StandardCharsets.UTF_8)
                .toString());

        userEntity = userRepository.save(userEntity);

        sendMessage(userDto, password);

        return modelMapper.map(userEntity, UserDto.class);
    }

    private String passwordGenerator() {
        return Base64.encodeBase64String(RandomStringUtils.randomAlphanumeric(10).getBytes());
    }

    private void sendMessage(final UserDto userDto, final String password) {
        final var message = MessageDto.builder()
                .to(userDto.getEmail())
                .message(String.format(MESSAGE, userDto.getEmail(), password))
                .subject(SUBJECT)
                .build();

        log.info("Message sent: {}", message);
    }
}
