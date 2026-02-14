    package com.pragma.usersMicroservice.domain.usecase;

    import com.pragma.usersMicroservice.application.dto.LoginRequest;
    import com.pragma.usersMicroservice.application.dto.UserRegisterRequest;
    import com.pragma.usersMicroservice.domain.api.IUserServicePort;
    import com.pragma.usersMicroservice.domain.exception.EmailAlreadyExistsException;
    import com.pragma.usersMicroservice.domain.exception.IdDocAlreadyExistsException;
    import com.pragma.usersMicroservice.domain.exception.UnderAgeException;
    import com.pragma.usersMicroservice.domain.model.Role;
    import com.pragma.usersMicroservice.domain.model.User;
    import com.pragma.usersMicroservice.domain.spi.IPasswordEncryptionPort;
    import com.pragma.usersMicroservice.domain.spi.IRolePersistencePort;
    import com.pragma.usersMicroservice.domain.spi.IUserPersistencePort;
    import com.pragma.usersMicroservice.domain.util.RoleName;
    import com.pragma.usersMicroservice.infrastructure.exception.RoleNotFoundException;

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
         * Validates if Id Document is already registered in the DB.
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
            // Validations
            validateAge(user);
            validateEmailAlreadyExists(user.getEmail());
            validateDocumentAlreadyExists(user.getIdDocument());
            //Get role from DB and set to User
            getAndSetUserRole(user, roleName);
            //Password encryption
            encryptPassword(user);
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

        @Override
        public LoginRequest registerCustomer(UserRegisterRequest request) {
            return saveUser(request, RoleName.OWNER);
        }


        /**
         * Encrypt a User password with BCrypt
         * @param user {@link User} with non-encrypted password
         */
        private void encryptPassword(User user){
            String encryptedPassword = passwordEncryptionPort.encode(user.getPassword());
            user.setPassword(encryptedPassword);
        }

        /**
         * Search for a role in DB and assign it to a user.
         * @param user {@link User} to change the role.
         * @param roleName {@link RoleName} String of RoleName Enum.
         */
        private void getAndSetUserRole(User user, RoleName roleName) {
            Role role = rolePersistencePort.findByName(roleName)
                    .orElseThrow(RoleNotFoundException::new);

            user.setRole(role);
        }



    }

