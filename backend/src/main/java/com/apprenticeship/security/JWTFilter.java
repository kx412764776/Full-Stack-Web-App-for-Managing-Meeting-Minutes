package com.apprenticeship.security;

import com.apprenticeship.utils.JWTUtil;
import com.apprenticeship.service.MemberDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class is responsible for filtering the requests
 * and checking when the client want to access if the token is valid.
 */
@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final MemberDetailsService memberDetailsService;

    public JWTFilter(JWTUtil jwtUtil, MemberDetailsService memberDetailsService) {
        this.jwtUtil = jwtUtil;
        this.memberDetailsService = memberDetailsService;
    }


    /**
     * This method is aimed to judge whether the request is with a Bearer token.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // If the header is null or doesn't start with "Bearer ", then the request is not filtered.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get the token from the header.
        String jwt = authHeader.substring(7);
        String subject = jwtUtil.getSubject(jwt);

        // If the subject is not null and the authentication is null, then the token is valid.
        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = memberDetailsService.loadUserByUsername(subject);

            // If the token is valid, then the user is authenticated.
            if (jwtUtil.isTokenValid(jwt, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                // Set the details of the authentication.
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Set the authentication to the context of the security.
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // Filter the request.
        filterChain.doFilter(request, response);
    }
}

