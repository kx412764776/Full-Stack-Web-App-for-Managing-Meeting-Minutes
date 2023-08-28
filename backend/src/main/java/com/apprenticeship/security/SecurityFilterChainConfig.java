package com.apprenticeship.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * This class is aimed to configure security filter chain.
 */
@Configuration
@EnableWebSecurity
public class SecurityFilterChainConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final JWTFilter jwtFilter;

    public SecurityFilterChainConfig(AuthenticationProvider authenticationProvider,
                                     AuthenticationEntryPoint authenticationEntryPoint,
                                     JWTFilter jwtFilter) {
        this.authenticationProvider = authenticationProvider;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.jwtFilter = jwtFilter;
    }

    /**
     * This method is aimed to configure the security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF
                .csrf(AbstractHttpConfigurer::disable)
                // using default configuration for cross-domain requests
                .cors(Customizer.withDefaults())
                // set the authorization for the requests
                .authorizeHttpRequests((authorizeHttpRequest) ->
                        authorizeHttpRequest.
                                // permit the requests to register and login, other requests need to be authenticated
                                requestMatchers(
                                        HttpMethod.POST,
                                        "/apprenticeship/register",
                                        "/apprenticeship/login"
                                )
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                // set the session management to stateless
                // so that the server will not store the session
                .sessionManagement((sessionManagement) ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // set the authentication provider
                .authenticationProvider(
                        authenticationProvider
                )
                // add jwt filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                )
                // handle the exception when the client is not authenticated
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPoint)
                );
        return http.build();
    }
}
