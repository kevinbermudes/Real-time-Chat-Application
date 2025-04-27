package org.example.proyectoauth.rest.auth.services.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.example.proyectoauth.rest.users.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Implementación del servicio {@link JwtService} para la gestión de tokens JWT.
 *
 * Esta clase utiliza la biblioteca Auth0 JWT para:
 * - Generar tokens firmados.
 * - Extraer claims.
 * - Validar tokens de autenticación.
 */
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String jwtSigninKey;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Extrae el nombre de usuario (subject) desde un token JWT.
     *
     * @param token Token JWT.
     * @return Nombre de usuario extraído.
     */
    @Override
    public String extractUserName(String token) {
        log.info("Extracting user name from token");
        return extractClaim(token, DecodedJWT::getSubject);
    }

    /**
     * Genera un nuevo token JWT para un usuario autenticado.
     *
     * @param userDetails Detalles del usuario.
     * @return Token JWT generado.
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        log.info("Generating token for user: {}", userDetails.getUsername());
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Valida que el token JWT corresponda al usuario autenticado y no haya expirado.
     *
     * @param token       Token JWT.
     * @param userDetails Detalles del usuario autenticado.
     * @return {@code true} si el token es válido, {@code false} en caso contrario.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        log.info("Validating token for user: {}", userDetails.getUsername());
        String username = extractUserName(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Genera un token JWT con claims adicionales.
     *
     * @param extraClaims Claims personalizados.
     * @param userDetails Usuario autenticado.
     * @return Token JWT generado.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        Algorithm algorithm = Algorithm.HMAC512(getSigningKey());
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + (1000 * jwtExpiration));

        log.info("✅ Generando token con subject (username): {}", userDetails.getUsername());

        return JWT.create()
                .withHeader(createHeader())
                .withSubject(userDetails.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(expirationDate)
                .withClaim("id", ((User) userDetails).getId())
                .withClaim("Rol", ((User) userDetails).getRoles().toString())// puedes agregar más claims si deseas
                .sign(algorithm);

    }

    /**
     * Extrae un claim específico desde un token JWT.
     *
     * @param token Token JWT.
     * @param claimsResolver Función para extraer el claim deseado.
     * @param <T> Tipo de dato del claim.
     * @return Claim extraído.
     */
    private <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        log.info("Extracting claim from token");
        DecodedJWT decodedJWT = JWT.decode(token);
        return claimsResolver.apply(decodedJWT);
    }

    /**
     * Verifica si el token JWT ha expirado.
     *
     * @param token Token JWT.
     * @return {@code true} si ha expirado.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración desde el token JWT.
     *
     * @param token Token JWT.
     * @return Fecha de expiración.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, DecodedJWT::getExpiresAt);
    }

    /**
     * Crea el encabezado (header) del JWT.
     *
     * @return Mapa con el encabezado.
     */
    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        return header;
    }

    /**
     * Codifica la clave de firma definida en las propiedades.
     *
     * @return Clave codificada.
     */
    private byte[] getSigningKey() {
        return Base64.getEncoder().encode(jwtSigninKey.getBytes());
    }
}
