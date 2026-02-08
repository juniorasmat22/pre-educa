package com.bootcodeperu.admision_academica.adapter.persistencia.mongo.repository;

import com.bootcodeperu.admision_academica.adapter.persistencia.mongo.document.Flashcard;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FlashcardMongoRepository extends MongoRepository<Flashcard, String> {
    List<Flashcard> findByIdTemaSQL(Long idTemaSQL);
}
