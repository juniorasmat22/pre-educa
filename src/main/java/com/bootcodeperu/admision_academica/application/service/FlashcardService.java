package com.bootcodeperu.admision_academica.application.service;

import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardRequest;
import com.bootcodeperu.admision_academica.application.controller.dto.flashcard.FlashcardResponse;

import java.util.List;

public interface FlashcardService {
    void createFlashcard(FlashcardRequest request);

    List<FlashcardResponse> getFlashcardsByTheme(Long themeId);
}
