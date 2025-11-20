package com.bootcodeperu.admision_academica.application.controller.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para manejar la solicitud de login.
 */
@Data // Genera Getters, Setters, toString, hashCode, equals
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;
}