package com.bootcodeperu.admision_academica.application.controller.dto;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Data;

@Data
public class PreguntaCreationDTO {
    private Long idTema;
    private String nivel; // Básico, Intermedio, Avanzado, Banco UNT
    private String tipoPregunta; // PracticaTema, BancoSimulacro
    private Integer anioExamen;
    
    // Contenido de la pregunta (lo que va a MongoDB)
    private String enunciado;
    private JsonNode opciones;
    private String respuestaCorrecta;
    private String explicacionCorrecta;
    private String explicacionIncorrecta;
}