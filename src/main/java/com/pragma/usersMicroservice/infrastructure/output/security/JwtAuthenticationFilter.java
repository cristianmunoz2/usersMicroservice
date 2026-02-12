package com.pragma.usersMicroservice.infrastructure.output.security;

import com.pragma.usersMicroservice.domain.spi.IJwtProviderPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtProviderPort jwtProviderPort;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        log.info("Starting JWT authentication filter for request: {}", request.getRequestURI());
        String token = resolveToken(request);

        if (token != null && jwtProviderPort.validateToken(token)) {
            log.info("Valid JWT token found for request: {}", request.getRequestURI());
            String email = jwtProviderPort.getEmailFromToken(token);
            log.info("Extracted email from token: {}", email);
            String role = jwtProviderPort.getRoleFromToken(token);
            log.info("Extracted role from token: {}", role);

            log.info("Setting authentication for user: {} with role: {}", email, role);
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + role)
            );
            log.info("Authorities set for user: {}: {}", email, authorities);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(email, null, authorities);
            log.info("Authentication object created for user: {}: {}", email, authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication set in SecurityContext for user: {}", email);
        }
        log.info("Proceeding with filter chain for request: {}", request.getRequestURI());
        chain.doFilter(request, response);
        log.info("Completed JWT authentication filter for request: {}", request.getRequestURI());
    }

    private String resolveToken(HttpServletRequest request) {
        log.info("Resolving JWT token from request headers for request: {}", request.getRequestURI());
        String bearerToken = request.getHeader("Authorization");
        log.info("Authorization header value: {}", bearerToken);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            log.info("Bearer token found in Authorization header for request: {}", request.getRequestURI());
            return bearerToken.substring(7);
        }
        return null;
    }
}
