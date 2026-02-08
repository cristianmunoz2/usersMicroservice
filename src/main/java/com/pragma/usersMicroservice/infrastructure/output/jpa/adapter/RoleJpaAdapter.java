package com.pragma.usersMicroservice.infrastructure.output.jpa.adapter;

import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.util.RoleName;
import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.RoleEntity;
import com.pragma.usersMicroservice.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.pragma.usersMicroservice.infrastructure.output.jpa.repository.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleJpaAdapter implements IRolePersistencePort {

    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;

    @Override
    public Role findByName(RoleName name) {
        RoleEntity roleEntityOptional = roleRepository.findByName(name);
        return roleEntityMapper.toRole(roleEntityOptional);
    }
}
