package com.bootcodeperu.admision_academica.domain.repository;

import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.Token;
import com.bootcodeperu.admision_academica.domain.model.Usuario;

public interface TokenRepository {
    
    Token save(Token token);
    
    void saveAll(List<Token> tokens); // Para marcar tokens anteriores como revocados
    
    Optional<Token> findByToken(String token);

    /**
     * Método clave para la seguridad JWT.
     * Busca todos los tokens que NO están revocados y NO han expirado 
     * asociados a un usuario específico.
     * * @param id El ID del usuario.
     * @return Lista de tokens válidos.
     */
    List<Token> findAllValidTokenByUser(Long id);
    /**
     * Obtiene el usuario asociado a un token específico.
     * Necesario para refreshToken.
     * @param token El JWT.
     * @return Usuario si existe.
     */
    Optional<Usuario> findUsuarioByToken(String token);
}