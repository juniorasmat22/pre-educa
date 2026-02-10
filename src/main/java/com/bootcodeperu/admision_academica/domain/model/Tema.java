package com.bootcodeperu.admision_academica.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tema", uniqueConstraints = {@UniqueConstraint(columnNames = {"idCurso", "nombreTema"})})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tema extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Curso (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "idCurso", nullable = false)
    private Curso curso;

    @Column(name = "nombreTema", nullable = false)
    private String nombreTema; // Ej: Ecuaciones de Segundo Grado
}
