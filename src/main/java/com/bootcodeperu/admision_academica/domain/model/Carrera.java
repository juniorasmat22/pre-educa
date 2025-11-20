package com.bootcodeperu.admision_academica.domain.model;

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
@Table(name = "carrera")
@Data
@NoArgsConstructor
public class Carrera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre; // Ej: Medicina, Ingeniería de Sistemas, Derecho
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArea")
    private Area area; // Área a la que pertenece (A, B, C, o D)
}