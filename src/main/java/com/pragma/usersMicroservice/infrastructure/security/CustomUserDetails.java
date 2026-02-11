package com.pragma.usersMicroservice.infrastructure.security;


import com.pragma.usersMicroservice.domain.model.User;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails, CredentialsContainer {

    //Indicates UserDetails User class
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public void eraseCredentials() {

    }

    //Spring interprets role as "ROLE_ROLENAME", so we need to concatenate
    //Here Spring want a SimpleGrantedAuthority Object. This object is initialized with a string ("ROLE_ROLENAME")
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole().getName().name()));
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
