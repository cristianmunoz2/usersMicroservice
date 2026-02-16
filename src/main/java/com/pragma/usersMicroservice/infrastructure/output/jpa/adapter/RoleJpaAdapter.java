package com.pragma.usersMicroservice.infrastructure.output.jpa.adapter;

import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.util.RoleName;
import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.RoleEntity;
import com.pragma.usersMicroservice.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usersMicroservice.infrastructure.output.jpa.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * JPA Adapter implementation for Role persistence.
 * <p>
 * Implements the {@link IRolePersistencePort} to interact with the database
 * via Spring Data JPA repositories, mapping entities to domain models.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class RoleJpaAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;

    /**
     * Retrieves a Role domain object by its name.
     *
     * @param name The {@link RoleName} enum to search for.
     * @return An Optional with the {@link RoleEntity} if exists.
     */
    @Override
    public Optional<Role> findByName(RoleName name) {
        Optional<RoleEntity> entity = roleRepository.findByName(name);
        return entity.map(roleEntityMapper::toRole);
    }

    @Override
    public Optional<Role> findById(Long id) {
        Optional<RoleEntity> entity = roleRepository.findById(id);
        return entity.map(roleEntityMapper::toRole);
    }
}