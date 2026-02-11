package com.pragma.usersMicroservice.infrastructure.security.config;

import com.pragma.usersMicroservice.infrastructure.output.jpa.entity.UserEntity;
import com.pragma.usersMicroservice.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.pragma.usersMicroservice.infrastructure.output.jpa.repository.IUserRepository;
import com.pragma.usersMicroservice.infrastructure.security.adapter.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .map(userEntityMapper::toUser)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
