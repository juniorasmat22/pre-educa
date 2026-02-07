package com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document;


import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "flashcards")
@Data
public class Flashcard {
    @Id
    private String id;
    private Long idTemaSQL;
    private String frente;      // Pregunta o Concepto
    private String dorso;       // Respuesta o Explicación
    private String imagenUrl;
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}