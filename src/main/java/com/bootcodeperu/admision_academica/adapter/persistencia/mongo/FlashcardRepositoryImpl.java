package com.bootcodeperu.admision_academica.adapter.persistencia.mongo;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.Flashcard;
import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository.FlashcardMongoRepository;
import com.bootcodeperu.admision_academica.domain.repository.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FlashcardRepositoryImpl implements FlashcardRepository {

    private final FlashcardMongoRepository repository;

    @Override
    public Flashcard save(Flashcard flashcard) {
        return repository.save(flashcard);
    }

    @Override
    public Optional<Flashcard> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Flashcard> findByTemaId(Long temaId) {
        return repository.findByIdTemaSQL(temaId);
    }

    @Override
    public void delete(String id) {
        repository.deleteById(id);
    }
}