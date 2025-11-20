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
@Table(name = "permiso")
@Data
@NoArgsConstructor
public class Permiso {
    public Permiso(String string) {
    	this.nombre=string;
	}

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre; // Ej: SIMULACRO_READ, CURSO_CREATE
}