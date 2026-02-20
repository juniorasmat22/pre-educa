package com.bootcodeperu.admision_academica.application.usercase;

import com.bootcodeperu.admision_academica.adapter.security.UsuarioPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final MeterRegistry meterRegistry; // Inyección obligatoria
    private Counter jwtGeneratedCounter;
    private Counter jwtSuccessCounter;
    private Counter jwtFailureCounter;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @PostConstruct
    public void initMetrics() {
        // Inicialización de contadores
        this.jwtGeneratedCounter = Counter.builder("admision.security.jwt.generated")
                .description("Total de tokens JWT creados")
                .register(meterRegistry);

        this.jwtSuccessCounter = Counter.builder("admision.security.jwt.success")
                .description("Total de validaciones JWT exitosas")
                .register(meterRegistry);

        this.jwtFailureCounter = Counter.builder("admision.security.jwt.failure")
                .description("Total de fallos en validación (Firmas inválidas o expirados)")
                .register(meterRegistry);
    }

    // Extrae el nombre de usuario (email) del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extrae un claim específico del token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Genera un token sin claims extra
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();

        // Si el userDetails es nuestro record, podemos meter datos extra en el JWT
        if (userDetails instanceof UsuarioPrincipal principal) {
            extraClaims.put("userId", principal.usuario().getId());
            extraClaims.put("role", principal.usuario().getRol().getNombre());
        }
        jwtGeneratedCounter.increment();
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        // Un refresh token no necesita claims extra, solo identifica al usuario
        jwtGeneratedCounter.increment();
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Genera un token con claims extra (puedes agregar roles, id de usuario, etc.)
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Valida si el token es válido y no ha expirado
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            boolean isValid = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

            if (isValid) {
                jwtSuccessCounter.increment(); // Métrica: Validación exitosa
            } else {
                jwtFailureCounter.increment(); // Métrica: Datos no coinciden
            }
            return isValid;
        } catch (Exception e) {
            jwtFailureCounter.increment(); // Métrica: Error en estructura o expirado
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Decodifica y extrae todos los claims
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Obtiene la clave de firma a partir del Secret Key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            boolean notExpired = !isTokenExpired(token);
            if (notExpired) jwtSuccessCounter.increment();
            else jwtFailureCounter.increment();
            return notExpired;
        } catch (Exception e) {
            jwtFailureCounter.increment(); // Captura firmas falsas o tokens corruptos
            return false;
        }
    }

}