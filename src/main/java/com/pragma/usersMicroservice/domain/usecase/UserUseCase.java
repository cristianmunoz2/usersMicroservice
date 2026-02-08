package com.pragma.usersMicroservice.domain.usecase;

import com.pragma.usersMicroservice.domain.api.IUserServicePort;
import com.pragma.usersMicroservice.domain.exception.UnderAgeException;
import com.pragma.usersMicroservice.domain.model.Role;
import com.pragma.usersMicroservice.domain.model.User;
import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersMicroservice.domain.util.RoleName;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Provides the implementation for user-related use cases
 */
public class UserUseCase implements IUserServicePort {


    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;

    /**
     * Constructor method
     * @param userPersistencePort
     * @param rolePersistencePort
     */

    public UserUseCase(IUserPersistencePort userPersistencePort, IRolePersistencePort rolePersistencePort) {
        this.userPersistencePort = userPersistencePort;
        this.rolePersistencePort = rolePersistencePort;
    }

    /**
     * Validates that the user is at least 18 years old.
     * @param user The user to validate.
     * @throws UnderAgeException if the user's age is less than 18.
     */

    private void validateUser(User user){
        if (user.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS) < 18L){
            throw new UnderAgeException();
        }
    }

    /**
     * Saves a user to the system after validation.
     * Sets the user's role and persists the data through the persistence port.
     * @param user The {@link User} to save.
     * @param roleName The name of the role ({@link RoleName}) to be assigned.
     * @return The saved {@link User}.
     * @throws UnderAgeException if the user is not of legal age.
     */

    private User saveUser(User user, RoleName roleName){
        validateUser(user);
        Role role = rolePersistencePort.findByName(roleName);
        user.setRole(role);
        return userPersistencePort.saveUser(user);
    }

    /**
     * Creates a new user with the Owner role
     * @param user {@link User} to be registered.
     * @return The saved {@link User}.
     */

    @Override
    public User createOwner(User user) {
        return saveUser(user, RoleName.OWNER);
    }




}

