package com.bootcodeperu.admision_academica.domain.model;

import java.time.LocalDateTime;

import com.bootcodeperu.admision_academica.domain.model.enums.EstadoSimulacro;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "resultadosimulacro", indexes = {
        @Index(name = "idx_res_usuario_estado", columnList = "idUsuario, estado"),
        @Index(name = "idx_res_estado_expiracion", columnList = "estado, fechaExpiracion")
})
@Data
@NoArgsConstructor
public class ResultadoSimulacro extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idArea", nullable = false)
    private Area areaEvaluada;

    private LocalDateTime fechaEvaluacion = LocalDateTime.now();
    private LocalDateTime fechaExpiracion;
    private Integer tiempoTomado; // En minutos
    private Double puntajeTotal;
    private Integer preguntasCorrectas = 0;
    private Integer preguntasIncorrectas = 0;
    private Integer preguntasEnBlanco = 0;
    // Mapeo del JSONB de PostgreSQL a un objeto Java (JsonNode de Jackson)
    @Column(name = "detallesrespuestas", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private JsonNode detallesRespuestas;
    @Enumerated(EnumType.STRING)
    private EstadoSimulacro estado;
}
