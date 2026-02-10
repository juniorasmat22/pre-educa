package com.bootcodeperu.admision_academica.domain.model;

import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Table(name = "rol")
@Data
@NoArgsConstructor
public class Rol extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre; // Ej: ROLE_ESTUDIANTE, ROLE_ADMIN

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rolpermiso",
            joinColumns = @JoinColumn(name = "idRol"),
            inverseJoinColumns = @JoinColumn(name = "idPermiso")
    )
    private Set<Permiso> permisos;
}