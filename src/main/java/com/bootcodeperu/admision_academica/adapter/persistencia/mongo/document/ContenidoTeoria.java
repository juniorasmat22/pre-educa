package com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "contenidoteoria") // Nombre de la colección en Mongo
@Data
@NoArgsConstructor
public class ContenidoTeoria {

    @Id
    private String id; // ID de MongoDB (ObjectId)

    private Long idTemaSQL; // FK: Referencia al ID del Tema en PostgreSQL

    private String titulo;
    
    private List<SeccionContenido> secciones;
    
    private LocalDateTime fechaCreacion = LocalDateTime.now();
    private LocalDateTime ultimaActualizacion = LocalDateTime.now();
}