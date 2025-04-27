package org.example.proyectoauth.rest.auth.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Interfaz para la gestión de tokens JWT (JSON Web Token).
 *
 * Define las operaciones esenciales para:
 * - Extraer información de un token.
 * - Generar nuevos tokens.
 * - Validar tokens existentes.
 */
public interface JwtService {

    /**
     * Extrae el nombre de usuario (username) contenido en el token JWT.
     *
     * @param token Token JWT desde el cual extraer la información.
     * @return Nombre de usuario extraído.
     */
    String extractUserName(String token);

    /**
     * Genera un nuevo token JWT basado en los detalles del usuario autenticado.
     *
     * @param userDetails Detalles del usuario autenticado.
     * @return Token JWT generado.
     */
    String generateToken(UserDetails userDetails);

    /**
     * Valida si el token JWT proporcionado es válido para el usuario dado.
     *
     * @param token       Token JWT a validar.
     * @param userDetails Detalles del usuario a verificar.
     * @return {@code true} si el token es válido, {@code false} en caso contrario.
     */
    boolean isTokenValid(String token, UserDetails userDetails);
}
