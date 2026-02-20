package com.bootcodeperu.admision_academica.adapter.scheduler;

import com.bootcodeperu.admision_academica.domain.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupScheduler {

    private final TokenRepository tokenRepository;

    // 🧹 OPCIÓN 2: Se ejecuta TODOS LOS DÍAS a las 3:00 AM
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional // CRÍTICO: Todo @Modifying en BD requiere @Transactional
    public void limpiarTokensMuertos() {
        log.info("Iniciando limpieza nocturna de Tokens JWT...");

        try {
            LocalDateTime ahora = LocalDateTime.now();
            // Ejecutamos el borrado masivo
            int eliminados = tokenRepository.eliminarTokensMuertos(ahora);

            log.info("Limpieza nocturna exitosa. Se eliminaron físicamente {} tokens caducados/revocados.", eliminados);
        } catch (Exception e) {
            log.error("Ocurrió un error al limpiar los tokens: {}", e.getMessage());
        }
    }
}