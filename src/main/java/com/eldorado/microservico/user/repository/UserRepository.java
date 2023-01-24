package com.eldorado.microservico.user.repository;

import com.eldorado.microservico.user.domain.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {
}
