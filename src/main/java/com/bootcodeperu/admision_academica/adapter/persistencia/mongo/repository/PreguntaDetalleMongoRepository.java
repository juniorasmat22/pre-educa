package com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.PreguntaDetalle;

public interface PreguntaDetalleMongoRepository extends MongoRepository<PreguntaDetalle, String>{
	// Método de negocio clave: Obtener el detalle de varias preguntas a la vez (para un examen o práctica)
    List<PreguntaDetalle> findAllByIdIn(List<String> ids);
}
