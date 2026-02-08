package com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document;

import org.springframework.data.mongodb.core.mapping.Document;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Document(collection = "preguntasdetalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaDetalle {
    // El ID de MongoDB (ObjectId)
    @Id
    private String id; // Este es el mongoIdPregunta de 24 caracteres

    private Long idTemaSQL; // Referencia para mantener contexto

    private String enunciado;

    // Usaremos un tipo genérico como JsonNode para mapear la lista de opciones complejas de Mongo
    private Map<String, Object> opciones;

    private String respuestaCorrecta;

    private Map<String, Object> explicacionDetallada;

    private String fuente;


}
