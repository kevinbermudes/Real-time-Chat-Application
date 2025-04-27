package org.example.proyectoauth.rest.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la respuesta enviada tras una autenticación exitosa.
 *
 * Contiene únicamente el token JWT que el cliente utilizará para autenticar
 * futuras solicitudes.
 *
 * Esta clase utiliza anotaciones de Lombok para generar automáticamente:
 * - Getters y setters (@Data)
 * - Constructores (@NoArgsConstructor, @AllArgsConstructor)
 * - Builder fluido (@Builder)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthResponseDto {

    /**
     * Token JWT generado para el usuario autenticado.
     * Este token debe ser incluido en futuras peticiones dentro del header Authorization.
     */
    private String token;
}
