package com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.ContenidoTeoria;

public interface ContenidoTeoriaMongoRepository extends MongoRepository<ContenidoTeoria, String> {
    
    /**
     * Método clave para el consumo: Obtener todo el contenido teórico de un tema.
     * @param idTemaSQL El ID del Tema de PostgreSQL.
     * @return Lista de elementos de contenido (ordenados por 'orden').
     *
     * Solución al error: Define la consulta JSON para filtrar por idTemaSQL y ordenar.
     * La parte de 'sort' se especifica en el segundo parámetro de @Query.
     */
    @Query(value = "{ 'idTemaSQL' : ?0 }", sort = "{ 'orden' : 1 }")
    List<ContenidoTeoria> findByTemaIdOrdered(Long idTemaSQL);
}
