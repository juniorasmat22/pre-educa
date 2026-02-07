package com.bootcodeperu.admision_academica.application.controller.dto.flashcard;

public record FlashcardResponse(
        String id, // Mongo ID
        Long idTema,
        String frente,
        String dorso,
        String imagenUrl
) {}