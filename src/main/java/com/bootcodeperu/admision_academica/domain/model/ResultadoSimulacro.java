package com.bootcodeperu.admision_academica.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
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
@Table(name = "resultadosimulacro")
@Data
@NoArgsConstructor
public class ResultadoSimulacro {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "areaEvaluada")
    private Area areaEvaluada;

    private LocalDateTime fechaEvaluacion = LocalDateTime.now();

    private Integer tiempoTomado; // En minutos

    @Column( nullable = false)
    private Double puntajeTotal;

    // Mapeo del JSONB de PostgreSQL a un objeto Java (JsonNode de Jackson)
    @Column(name = "detallesrespuestas", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode detallesRespuestas;
}
