package com.pragma.usersMicroservice.infrastructure.output.security;

import com.pragma.usersMicroservice.domain.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementation of Spring Security's UserDetails interface.
 * <p>
 * This adapter wraps the domain User model to make it compatible with Spring Security's
 * authentication and authorization mechanisms, maintaining the separation of concerns
 * in the hexagonal architecture.
 * </p>
 */
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    @Getter
    private final User user;

    /**
     * Returns the authorities granted to the user.
     * Maps the domain role to Spring Security's GrantedAuthority.
     *
     * @return Collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = "ROLE_" + user.getRole().getName().toString();
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    /**
     * Returns the password used to authenticate the user.
     *
     * @return The encrypted password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Returns the username used to authenticate the user.
     * In this case, we use the email as the username.
     *
     * @return The user's email.
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * Indicates whether the user's account has expired.
     *
     * @return true if the account is valid (not expired).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked.
     *
     * @return true if the user is not locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) has expired.
     *
     * @return true if the credentials are valid (not expired).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled.
     *
     * @return true if the user is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

