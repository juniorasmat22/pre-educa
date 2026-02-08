package com.bootcodeperu.admision_academica.domain.repository;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.Flashcard;

import java.util.List;
import java.util.Optional;

public interface FlashcardRepository {
    Flashcard save(Flashcard flashcard);

    List<Flashcard> findByTemaId(Long temaId);

    Optional<Flashcard> findById(String id);

    void delete(String id);
}