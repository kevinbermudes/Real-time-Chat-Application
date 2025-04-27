package org.example.proyectoauth.rest.auth.services.authentication;

import org.example.proyectoauth.rest.auth.dto.JwtAuthResponseDto;
import org.example.proyectoauth.rest.auth.dto.UserSignInRequest;
import org.example.proyectoauth.rest.auth.dto.UserSignUpRequest;

/**
 * Interfaz que define las operaciones principales de autenticación en el sistema.
 *
 * Incluye métodos para registrar nuevos usuarios y autenticar usuarios existentes.
 * Devuelve un token JWT como respuesta a operaciones exitosas.
 */
public interface AuthenticationService {

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param signUpRequest Objeto con los datos requeridos para el registro.
     * @return {@link JwtAuthResponseDto} que contiene el token JWT generado.
     */
    JwtAuthResponseDto signUp(UserSignUpRequest signUpRequest);

    /**
     * Autentica un usuario existente en el sistema.
     *
     * @param signInRequest Objeto con las credenciales del usuario.
     * @return {@link JwtAuthResponseDto} que contiene el token JWT generado.
     */
    JwtAuthResponseDto signIn(UserSignInRequest signInRequest);
}
