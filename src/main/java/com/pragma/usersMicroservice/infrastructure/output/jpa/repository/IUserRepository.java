package com.pragma.usersMicroservice.infrastructure.output.jpa.repository;

import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {

}
