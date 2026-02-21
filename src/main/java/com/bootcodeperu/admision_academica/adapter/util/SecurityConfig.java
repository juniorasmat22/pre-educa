package com.bootcodeperu.admision_academica.adapter.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {
        http
                // 1. HABILITAR CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2. Deshabilitar CSRF (común cuando se usa JWT)
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exc -> exc
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                )
                .authorizeHttpRequests(authz -> authz
                        // Rutas públicas (registro, login, obtención de estructura inicial)
                        .requestMatchers("/api/v1/auth/**", "/api/v1/estructura/**", "/actuator/**").permitAll()
                        // Rutas que requieren autenticación
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                // Añadir el filtro JWT antes del filtro de usuario/contraseña de Spring
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Aquí pones la URL exacta de tu frontend (sin la barra diagonal final '/')
        configuration.setAllowedOrigins(List.of("http://localhost:4000"));

        // Métodos permitidos (OPTIONS es el crucial para el preflight)
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // Cabeceras permitidas
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Si usas cookies o tokens en los headers, esto debe ser true
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica esta configuración a TODAS las rutas de tu API
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}