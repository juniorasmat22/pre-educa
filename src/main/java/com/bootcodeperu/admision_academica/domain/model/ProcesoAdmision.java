package com.bootcodeperu.admision_academica.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcesoAdmision extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre; // ej. "Examen Ordinario 2026-II"
    private LocalDate fechaExamen; // ej. 2026-09-20
    private boolean vigente = true;
    @ManyToOne
    @JoinColumn(name = "id_universidad")
    private Universidad universidad;
}