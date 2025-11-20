package com.bootcodeperu.admision_academica.domain.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "outbox")
@Data
@NoArgsConstructor
public class Outbox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipoEvento; // Ej: PREGUNTA_CREADA_EVENT
    
    @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload; // Los datos del evento en formato JSON (Ej: DTO de la pregunta)

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(nullable = false)
    private String estado = "PENDIENTE"; // PENDIENTE, EN_PROCESO, COMPLETADO, ERROR
}