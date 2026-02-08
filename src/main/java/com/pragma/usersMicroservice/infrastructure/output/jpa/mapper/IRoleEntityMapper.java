package com.pragma.usersMicroservice.infrastructure.output.jpa.mapper;

import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRoleEntityMapper {
    RoleEntity toEntity(Role role);
    Role toRole(RoleEntity roleEntity);
}
