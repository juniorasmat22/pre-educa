package com.bootcodeperu.admision_academica.adapter.persistencia.postgres.springdata;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bootcodeperu.admision_academica.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bootcodeperu.admision_academica.domain.model.Token;

public interface SpringTokenRepository extends JpaRepository<Token, Long> {
    // Consulta clave para validar un token: buscar un token que no esté expirado ni revocado
    @Query(value = "SELECT t FROM Token t INNER JOIN Usuario u ON t.usuario.id = u.id " +
            "WHERE u.id = :id AND (t.revocado = false AND t.expirado = false)")
    List<Token> findAllValidTokenByUser(@Param("id") Long id);

    Optional<Token> findByToken(String token);

    // Obtener el usuario asociado a un token
    @Query("SELECT t.usuario FROM Token t WHERE t.token = :token")
    Optional<Usuario> findUsuarioByToken(@Param("token") String token);

    @Query("SELECT t FROM Token t WHERE t.usuario.id = :usuarioId AND t.fechaExpiracion < :ahora AND t.expirado = false")
    List<Token> encontrarTokensVencidosPorUsuario(@Param("usuarioId") Long usuarioId, @Param("ahora") LocalDateTime ahora);

    // Para la Opción 2: ¡BORRADO MASIVO DIRECTO EN POSTGRES!
    @Modifying
    @Query("DELETE FROM Token t WHERE t.fechaExpiracion < :ahora OR t.revocado = true")
    int eliminarTokensMuertos(@Param("ahora") LocalDateTime ahora);
}