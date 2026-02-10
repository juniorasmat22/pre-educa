package com.bootcodeperu.admision_academica.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpcionDetalle {
    private String texto;            // "6.022 x 10^23"
    private String feedback;         // "Correcto, es el número de Avogadro." o "Incorrecto, ese es el valor de la constante R."
    private Boolean esCorrecta;      // Opcional, ayuda a la lógica del frontend
}