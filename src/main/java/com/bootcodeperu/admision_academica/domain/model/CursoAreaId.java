package com.bootcodeperu.admision_academica.domain.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class CursoAreaId implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long idArea; // Mapea a Area.id
    private Long idCurso; // Mapea a Curso.id
}
