package com.bootcodeperu.admision_academica.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "cursoarea")
@Data // Genera getters, setters, toString, equals y hashCode
public class CursoArea {
	// Usamos el ID compuesto
    @EmbeddedId
    private CursoAreaId id;

    // Relaciones (usando @MapsId para mapear los campos del ID)
    @MapsId("idArea")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArea", referencedColumnName = "id")
    private Area area;

    @MapsId("idCurso")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCurso", referencedColumnName = "id")
    private Curso curso;

    @Column(name = "cantidadpreguntas", nullable = false)
    private Integer cantidadPreguntas;
}
