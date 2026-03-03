package com.pragma.usersmicroservice.infrastructure.output.jpa.mapper;

import com.pragma.usersmicroservice.domain.model.User;
import com.pragma.usersmicroservice.infrastructure.output.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface to convert between Domain User objects and JPA User entities.
 * Handles the translation of data between the domain layer and the persistence layer.
 */
@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserEntityMapper {

    /**
     * Converts a User domain object to a UserEntity for persistence.
     * <p>
     * Explicitly maps the Role object's ID from the domain to the roleId field
     * in the database entity.
     * </p>
     * @param user The domain object to convert.
     * @return The entity ready to be saved in the database.
     */
    @Mapping(target = "roleId", source = "role.id")
    UserEntity toEntity(User user);

    /**
     * Converts a UserEntity from the database to a User domain object.
     * * @param userEntity The entity retrieved from the database.
     * @return The reconstructed domain object.
     */
    @Mapping(target = "role.id", source = "roleId") // Opcional: si quieres reconstruir el ID del rol al leer
    User toUser(UserEntity userEntity);
}