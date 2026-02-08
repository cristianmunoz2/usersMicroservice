package com.pragma.usersMicroservice.infrastructure.output.jpa.adapter;

import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.util.RoleName;
import com.pragma.usersMicroservice.infrastructure.exception.RoleNotFoundException;
import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.RoleEntity;
import com.pragma.usersMicroservice.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usersMicroservice.infrastructure.output.jpa.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
     * @return The mapped {@link Role} domain object from the database.
     * @throws RoleNotFoundException if the role name was not found in DB.
     */
    @Override
    public Role findByName(RoleName name) {
        try{
            RoleEntity roleEntity = roleRepository.findByName(name);
            return roleEntityMapper.toRole(roleEntity);
        } catch (Exception e){
            throw new RoleNotFoundException();
        }
    }
}