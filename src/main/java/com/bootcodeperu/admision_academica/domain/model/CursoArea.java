package com.bootcodeperu.admision_academica.domain.model;

import com.bootcodeperu.admision_academica.domain.exception.DomainValidationException;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "cursoarea")
@Getter
@Setter
@ToString(exclude = {"area", "curso"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CursoArea {
    @EqualsAndHashCode.Include
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

    protected CursoArea() {
    }

    public CursoArea(Area area, Curso curso, Integer cantidadPreguntas) {
        this.area = area;
        this.curso = curso;
        this.cantidadPreguntas = cantidadPreguntas;
        this.id = new CursoAreaId(area.getId(), curso.getId());
    }

    public void actualizarCantidadPreguntas(Integer cantidad) {
        if (cantidad == null || cantidad < 0) {
            throw new DomainValidationException(
                    "La cantidad de preguntas debe ser mayor o igual a 0"
            );
        }
        this.cantidadPreguntas = cantidad;
    }
}
