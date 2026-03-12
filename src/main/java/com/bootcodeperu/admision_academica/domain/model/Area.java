package com.bootcodeperu.admision_academica.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "area")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Area extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre; // Ej: A. Ciencias de la Vida y la Salud

    @Column(name = "descripcion", nullable = false)
    private String descripcion; // Ej: A

    private Double puntajeCorrecta; // Ej: 4.00, 5.00, 6.00 (mayor peso)
    private Double puntajeIncorrecta; // Ej: -0.25
    private Double puntajeBlanco; // Ej: -0.25
    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_universidad", nullable = true)
    private Universidad universidad;
    @OneToMany(mappedBy = "area", fetch = FetchType.LAZY)
    private Set<Carrera> carreras;
}
