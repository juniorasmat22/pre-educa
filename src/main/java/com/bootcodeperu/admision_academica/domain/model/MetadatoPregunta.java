package com.bootcodeperu.admision_academica.domain.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "metadatopregunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadatoPregunta {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Tema
    @ManyToOne
    @JoinColumn(name = "idTema", nullable = false)
    private Tema tema;

    @Column(name = "mongoIdPregunta", unique = true, length = 24)
    private String mongoIdPregunta; // El ID de 24 caracteres de MongoDB

    @Column(name = "nivel", nullable = false, length = 50)
    private String nivel; // 'Básico', 'Intermedio', 'Avanzado', 'Banco UNT'

    @Column(name = "tipoPregunta", nullable = false, length = 50)
    private String tipoPregunta; // 'PracticaTema', 'BancoSimulacro'

    @Column(name = "anioExamen")
    private Integer anioExamen;

    @Column(name = "fechaCreacion")
    private LocalDateTime fechaCreacion;
}
