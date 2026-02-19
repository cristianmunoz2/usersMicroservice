package com.pragma.usersmicroservice.infrastructure.output.security;

import com.pragma.usersmicroservice.domain.model.Role;
import com.pragma.usersmicroservice.domain.model.User;
import com.pragma.usersmicroservice.domain.spi.IRolePersistencePort;
import com.pragma.usersmicroservice.domain.spi.IUserPersistencePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Custom implementation of Spring Security's UserDetailsService.
 * <p>
 * This service loads user-specific data from the domain layer and adapts it
 * to Spring Security's UserDetails interface, enabling authentication and authorization
 * while maintaining the hexagonal architecture's separation of concerns.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final IUserPersistencePort userPersistencePort;
    private final IRolePersistencePort rolePersistencePort;

    /**
     * Loads the user by their email (username).
     * <p>
     * This method is called by Spring Security during the authentication process
     * to retrieve user details from the database.
     * </p>
     *
     * @param email The email (username) of the user to load.
     * @return UserDetails object containing user information and authorities.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user details for email: {}", email);

        Optional<User> userOptional = userPersistencePort.findByEmail(email);

        if (userOptional.isEmpty()) {
            log.error("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        User user = userOptional.get();

        // Load the complete role information if not fully loaded
        if (user.getRole() != null && user.getRole().getId() != null) {
            Optional<Role> roleOptional = rolePersistencePort.findById(Long.parseLong(user.getRole().getId()));
            roleOptional.ifPresent(user::setRole);
        }

        log.info("User loaded successfully: {} with role: {}",
                user.getEmail(),
                user.getRole() != null ? user.getRole().getName() : "NO_ROLE");

        return new UserDetailsImpl(user);
    }
}

