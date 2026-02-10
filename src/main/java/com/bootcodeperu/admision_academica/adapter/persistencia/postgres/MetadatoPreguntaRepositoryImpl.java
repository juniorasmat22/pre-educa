package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.enums.QuestionTarget;
import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringMetadatoPreguntaRepository;
import com.bootcodeperu.admision_academica.domain.model.MetadatoPregunta;
import com.bootcodeperu.admision_academica.domain.repository.MetadatoPreguntaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MetadatoPreguntaRepositoryImpl implements MetadatoPreguntaRepository {

    private final SpringMetadatoPreguntaRepository springMetadatoPreguntaRepository;

    @Override
    public MetadatoPregunta save(MetadatoPregunta metadato) {
        return springMetadatoPreguntaRepository.save(metadato);
    }

    @Override
    public List<MetadatoPregunta> findByTemaIdAndNivel(Long temaId, String nivel) {
        return springMetadatoPreguntaRepository.findByTemaIdAndNivel(temaId, nivel);
    }

    @Override
    public List<MetadatoPregunta> findByTemaIdAndNivelAndTarget(Long temaId, String nivel, QuestionTarget target) {
        return springMetadatoPreguntaRepository.findByTemaIdAndNivelAndTarget(temaId, nivel, target);
    }

    @Override
    public List<MetadatoPregunta> findByTemaIdAndTarget(Long temaId, QuestionTarget target) {
        return springMetadatoPreguntaRepository.findByTemaIdAndTarget(temaId, target);
    }

    @Override
    public Long countByTemaIdInAndTarget(List<Long> temaIds, QuestionTarget target) {
        // Usa el método de conteo de Spring Data JPA
        return springMetadatoPreguntaRepository.countByTemaIdInAndTarget(temaIds, target);
    }

    @Override
    public List<MetadatoPregunta> findRandomByTemaIdInAndTarget(
            List<Long> temaIds, String target,
            int limit) {
        // Usa la consulta nativa de selección aleatoria
        return springMetadatoPreguntaRepository.findRandomByTemaIdInAndTarget(temaIds, target, limit);
    }

    @Override
    public Optional<MetadatoPregunta> findByMongoIdPregunta(String mongoId) {
        return springMetadatoPreguntaRepository.findByMongoIdPregunta(mongoId);
    }

    @Override
    public Optional<MetadatoPregunta> findById(Long id) {
        // TODO Auto-generated method stub
        return springMetadatoPreguntaRepository.findById(id);
    }
}