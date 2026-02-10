package com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document;

import com.bootcodeperu.admision_academica.domain.model.OpcionDetalle;
import org.springframework.data.mongodb.core.mapping.Document;


import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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
    private String enunciadoImagenUrl;
    private Map<String, OpcionDetalle> opciones;
    private String respuestaCorrecta;
    private String explicacionCorrecta;
    private String explicacionIncorrecta;
    private String videoExplicativoUrl;
    private String fuente;
    private List<String> etiquetas;
    private Boolean verificado;
    private Integer dificultadPercibida;
}
