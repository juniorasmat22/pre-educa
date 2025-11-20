package com.bootcodeperu.admision_academica.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "token")
@Data
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 500) // Mayor longitud para el token JWT
    private String token;

    @Column(length = 50)
    private String tipoToken = "BEARER";

    private boolean revocado;

    private boolean expirado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
}