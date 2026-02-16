package com.pragma.usersMicroservice.infrastructure.output.jpa.mapper;

import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting between Role domain objects and JPA entities.
 * <p>
 * Handles the data transformation required to move Role information between
 * the Domain layer and the Infrastructure persistence layer.
 * </p>
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IRoleEntityMapper {

    /**
     * Maps a domain Role to a persistence Entity.
     *
     * @param role The domain object to be converted.
     * @return The JPA entity ready for database operations.
     */
    RoleEntity toEntity(Role role);

    /**
     * Maps a persistence Entity to a domain Role.
     *
     * @param roleEntity The entity retrieved from the database.
     * @return The reconstructed domain object.
     */
    Role toRole(RoleEntity roleEntity);
}