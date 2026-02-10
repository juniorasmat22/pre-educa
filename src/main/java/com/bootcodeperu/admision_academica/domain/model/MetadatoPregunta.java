package com.bootcodeperu.admision_academica.domain.model;

import java.time.LocalDateTime;

import com.bootcodeperu.admision_academica.domain.model.enums.QuestionTarget;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "metadatopregunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadatoPregunta extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación con Tema
    @ManyToOne
    @JoinColumn(name = "idTema", nullable = false)
    private Tema tema;

    @Column(name = "mongoIdPregunta", unique = true, length = 24)
    private String mongoIdPregunta; // El ID de 24 caracteres de MongoDB

    @Enumerated(EnumType.STRING)
    private QuestionTarget target; // Distingue el uso

    @Column(name = "nivel", nullable = false, length = 50)
    private String nivel; // 'Básico', 'Intermedio', 'Avanzado', 'Banco UNT'

    //@Column(name = "tipoPregunta", nullable = false, length = 50)
    //private String tipoPregunta; // 'PracticaTema', 'BancoSimulacro'

    @Column(name = "anioExamen")
    private Integer anioExamen;

//    @Column(name = "fechaCreacion")
//    private LocalDateTime fechaCreacion;
}
