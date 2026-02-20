package com.bootcodeperu.admision_academica.domain.model;

import com.bootcodeperu.admision_academica.domain.model.enums.Plataforma;
import com.bootcodeperu.admision_academica.domain.model.enums.TipoToken;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TipoToken tipoToken = TipoToken.ACCESS;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Plataforma plataforma = Plataforma.DESCONOCIDA;
    private boolean revocado;

    private boolean expirado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario")
    private Usuario usuario;
    @Column(name = "fecha_expiracion")
    private LocalDateTime fechaExpiracion;
}