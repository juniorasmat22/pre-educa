package com.bootcodeperu.admision_academica.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "area")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre; // Ej: A. Ciencias de la Vida y la Salud
    
    @Column(name = "descripcion", nullable = false, unique = true)
    private String descripcion; // Ej: A
    
    private Double puntajeCorrecta; // Ej: 4.00, 5.00, 6.00 (mayor peso)
    private Double puntajeIncorrecta; // Ej: -0.25
    private Double puntajeBlanco; // Ej: -0.25
}
