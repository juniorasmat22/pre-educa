package com.bootcodeperu.admision_academica.adapter.mapper;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.Flashcard;
import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlashcardMapper {

    // Si los nombres de los campos son iguales, MapStruct lo hace automático
    FlashcardResponse toResponse(Flashcard domain);

    // Si quieres mapear desde el DTO de creación al Documento de Mongo
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechaCreacion", expression = "java(java.time.LocalDateTime.now())")
    Flashcard toDocument(Object request); // Cambia Object por tu FlashcardRequestDTO
}
