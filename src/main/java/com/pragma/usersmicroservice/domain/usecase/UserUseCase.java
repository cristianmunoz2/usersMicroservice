package com.pragma.usersmicroservice.domain.usecase;

import com.pragma.usersmicroservice.domain.api.IUserServicePort;
import com.pragma.usersmicroservice.domain.exception.EmailAlreadyExistsException;
import com.pragma.usersmicroservice.domain.exception.IdDocAlreadyExistsException;
import com.pragma.usersmicroservice.domain.exception.UnderAgeException;
import com.pragma.usersmicroservice.domain.model.Role;
import com.pragma.usersmicroservice.domain.model.User;
import com.pragma.usersmicroservice.domain.spi.IPasswordEncryptionPort;
import com.pragma.usersmicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersmicroservice.domain.spi.IUserPersistencePort;
import com.pragma.usersmicroservice.domain.util.RoleName;
import com.pragma.usersmicroservice.infrastructure.exception.RoleNotFoundException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Provides the implementation for user-related use cases
 */
public class UserUseCase implements IUserServicePort {


    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;
    private final IPasswordEncryptionPort passwordEncryptionPort;

    /**
     * Constructor method
     * @param userPersistencePort User persistence port
     * @param rolePersistencePort Role persistence port
     * @param passwordEncryptionPort Password encryption port
     */

    public UserUseCase(IUserPersistencePort userPersistencePort,
                       IRolePersistencePort rolePersistencePort,
                       IPasswordEncryptionPort passwordEncryptionPort) {
        this.userPersistencePort = userPersistencePort;
        this.rolePersistencePort = rolePersistencePort;
        this.passwordEncryptionPort = passwordEncryptionPort;
    }

    /**
     * Validates that the user is at least 18 years old.
     * @param user The user to validate.
     * @throws UnderAgeException if the user's age is less than 18.
     */

    private void validateAge(User user){
        if (user.getBirthDate().until(LocalDate.now(), ChronoUnit.YEARS) < 18L){
            throw new UnderAgeException();
        }
    }

    /**
     * Validates if email is already registered in the DB.
     * @param email Email to validate
     */
    private void validateEmailAlreadyExists(String email){
        if(userPersistencePort.existsByEmail(email)){
            throw new EmailAlreadyExistsException();
        }
    }

    /**
     * Validates if id Document is already registered in the DB.
     * @param idDocument Id document number to validate
     */
    private void validateDocumentAlreadyExists(String idDocument){
        if(this.userPersistencePort.existsByIdDocument(idDocument)){
            throw new IdDocAlreadyExistsException();
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

    public User saveUser(User user, RoleName roleName){
        validateAge(user);
        validateEmailAlreadyExists(user.getEmail());
        validateDocumentAlreadyExists(user.getIdDocument());
        
        Role role = rolePersistencePort.findByName(roleName)
                .orElseThrow(RoleNotFoundException::new);
        
        String encryptedPassword = passwordEncryptionPort.encode(user.getPassword());
        
        User userToSave = User.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .idDocument(user.getIdDocument())
                .phone(user.getPhone())
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .password(encryptedPassword)
                .role(role)
                .build();
        
        return userPersistencePort.saveUser(userToSave);
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

    /**
     * Creates a new user with the Employee role
     * @param user {@link User} to be registered.
     */
    @Override
    public void createEmployee(User user) {
        saveUser(user, RoleName.EMPLOYEE);
    }

    @Override
    public void createCustomer(User user) {
        saveUser(user, RoleName.CUSTOMER);
    }

    /**
     * Checks if a user exists by their ID.
     * @param id The ID of the user to check.
     * @return true if the user exists, false otherwise.
     */
    @Override
    public boolean existsById(String id) {
        return userPersistencePort.existsById(id);
    }

    @Override
    public String getPhoneById(String id) {
        return userPersistencePort.getPhoneById(id);
    }

    @Override
    public String findEmailById(String id) {
        if (!userPersistencePort.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        return userPersistencePort.findEmailById(id);
    }


}

