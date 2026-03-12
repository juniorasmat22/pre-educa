package com.bootcodeperu.admision_academica.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "universidad")
@Getter
@Setter
public class Universidad extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre; // ej. "Universidad Nacional de Trujillo"
    private String siglas; // ej. "UNT"
    @OneToMany(mappedBy = "universidad", fetch = FetchType.LAZY)
    private Set<Area> areas;
}