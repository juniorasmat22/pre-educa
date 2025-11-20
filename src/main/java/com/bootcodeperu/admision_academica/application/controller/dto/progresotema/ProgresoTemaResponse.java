package com.bootcodeperu.admision_academica.application.controller.dto.progresotema;

import com.bootcodeperu.admision_academica.application.controller.dto.tema.TemaResponse;

public record ProgresoTemaResponse(Long id,
	    Long idUsuario,
	    TemaResponse tema,
	    Boolean teoriaCompletada,
	    Double puntajePromedio,
	    Integer numeroIntentos) {

}
