package com.bootcodeperu.admision_academica.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ObjetivoAcademico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_carrera")
    private Carrera carrera; // Al saber la carrera, implícitamente ya sabemos el Área y la Universidad

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_proceso_admision")
    private ProcesoAdmision procesoAdmision; // Para saber a qué fecha apunta (puede ser null si solo se prepara sin fecha)

    private boolean postulaProximoExamen; // true si va a dar el examen, false si solo está estudiando a futuro

    private boolean objetivoPrincipal; // true para la universidad que se muestra al abrir el app, false para las secundarias

    private LocalDateTime fechaRegistro;
}