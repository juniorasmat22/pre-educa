package com.bootcodeperu.admision_academica.adapter.persistencia.postgres;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata.SpringTokenRepository;
import com.bootcodeperu.admision_academica.domain.model.Token;
import com.bootcodeperu.admision_academica.domain.repository.TokenRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final SpringTokenRepository springTokenRepository;

    @Override
    public Token save(Token token) {
        return springTokenRepository.save(token);
    }

    @Override
    public Optional<Token> findByToken(String token) {
        return springTokenRepository.findByToken(token);
    }

    @Override
    public List<Token> findAllValidTokenByUser(Long id) {
        return springTokenRepository.findAllValidTokenByUser(id);
    }

    @Override
    public void saveAll(List<Token> tokens) {
        springTokenRepository.saveAll(tokens);
    }
}