package com.bootcodeperu.admision_academica.domain.model;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "progresotema", uniqueConstraints = {@UniqueConstraint(columnNames = {"idUsuario", "idTema"})})
@Data
@NoArgsConstructor
public class ProgresoTema {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTema", nullable = false)
    private Tema tema;

    @ColumnDefault("false")
    private Boolean teoriaCompletada = false;

    private Double puntajePromedio= 0.0; // Usamos Double o BigDecimal en Java para el DECIMAL de SQL
    
    @ColumnDefault("0")
    private Double sumaPuntajes = 0.0; // <<< NUEVO: Suma de todos los puntajes obtenidos
    
    @ColumnDefault("0")
    private Integer numeroIntentos = 0; // <<< NUEVO: Contador de cuántas veces practicó
}
