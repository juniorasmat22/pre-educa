package com.bootcodeperu.admision_academica.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carrera")
@Data
@NoArgsConstructor
public class Carrera extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre; // Ej: Medicina, Ingeniería de Sistemas, Derecho

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArea")
    private Area area; // Área a la que pertenece (A, B, C, o D)
    @Column(name = "puntaje_minimo_historico")
    private Double puntajeMinimoHistorico;
}