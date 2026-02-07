package com.pragma.usersMicroservice.application.mapper;

import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;
import com.pragma.usersMicroservice.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * Mapper interface for converting user registration data.
 * <p>
 * This component maps the {@link UserRegisterRequest} DTO from the application layer
 * to the {@link User} domain model, ensuring a clean separation between
 * external request formats and internal business logic.
 * </p>
 */
@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IUserRegisterRequestMapper {

    /**
     * Maps a registration request to the User domain entity.
     * * @param userRegisterRequest The DTO containing the user registration data.
     * @return A {@link User} domain object populated with the request data.
     */
    @Mapping(target = "role", ignore = true)
    User toUser(UserRegisterRequest userRegisterRequest);
}