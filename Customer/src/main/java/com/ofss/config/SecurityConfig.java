package com.ofss.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ofss.security.JwtAuthFilter;
import com.ofss.security.RestAccessDeniedHandler;
import com.ofss.security.RestAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;
    private final RestAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            RestAuthenticationEntryPoint authenticationEntryPoint,
            RestAccessDeniedHandler accessDeniedHandler
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity
    ) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .authorizeHttpRequests(authorize -> authorize

                        // USER: only their own profile
                        .requestMatchers(HttpMethod.GET, "/api/customers/me")
                        .hasRole("USER")

                        // ADMIN and MANAGER: read any Customer record
                        .requestMatchers(HttpMethod.GET, "/api/customers")
                        .hasAnyRole("ADMIN", "MANAGER")

                        .requestMatchers(HttpMethod.GET, "/api/customers/**")
                        .hasAnyRole("ADMIN", "MANAGER")

                        // ADMIN: all write operations
                        .requestMatchers(HttpMethod.POST, "/api/customers")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/customers/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/customers/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/customers/**")
                        .hasRole("ADMIN")

                        .anyRequest()
                        .authenticated()
                )
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return httpSecurity.build();
    }
}