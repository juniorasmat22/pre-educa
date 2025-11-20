package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bootcodeperu.admision_academica.domain.model.Tema;

public interface SpringTemaRepository extends JpaRepository<Tema, Long> {
    List<Tema> findAllByCursoId(Long cursoId);
    @Query("SELECT t.id FROM Tema t WHERE t.curso.id = :cursoId") // Consulta directa para IDs
    List<Long> findIdsByCursoId(@Param("cursoId") Long cursoId);
}