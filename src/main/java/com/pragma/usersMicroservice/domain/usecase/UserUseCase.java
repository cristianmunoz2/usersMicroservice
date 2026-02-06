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

public class UserUseCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;

    public UserUseCase(IUserPersistencePort userPersistencePort, IRolePersistencePort rolePersistencePort) {
        this.userPersistencePort = userPersistencePort;
        this.rolePersistencePort = rolePersistencePort;
    }

    @Override
    public User createOwner(User user) {
        return saveUser(user, RoleName.OWNER);
    }

    private User saveUser(User user, RoleName roleName){
        validateUser(user);
        Role role = rolePersistencePort.findByName(roleName);
        user.setRole(role);
        return userPersistencePort.saveUser(user);
    }

    private void validateUser(User user){
        if (user.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS) < 18L){
            throw new UnderAgeException();
        }
    }
}

