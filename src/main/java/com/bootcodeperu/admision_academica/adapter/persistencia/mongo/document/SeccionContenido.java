package com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeccionContenido {
    
    private Integer orden; // Para mantener el orden de presentación
    private String tipo;   // Ej: 'VIDEO', 'TEXTO', 'IMAGEN', 'PDF'
    private String subtitulo; 
    
    // Contenido: solo uno de estos debe tener valor según el tipo
    private String texto;
    private String url;
}