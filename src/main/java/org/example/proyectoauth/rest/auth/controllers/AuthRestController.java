package org.example.proyectoauth.rest.auth.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.proyectoauth.rest.auth.dto.JwtAuthResponseDto;
import org.example.proyectoauth.rest.auth.dto.UserSignInRequest;
import org.example.proyectoauth.rest.auth.dto.UserSignUpRequest;
import org.example.proyectoauth.rest.auth.services.authentication.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para gestionar la autenticación de usuarios.
 *
 * Este controlador expone endpoints públicos para:
 * - Registrar nuevos usuarios.
 * - Autenticar usuarios existentes.
 *
 * También maneja las excepciones de validación asociadas a las solicitudes.
 */
@RestController
@Slf4j
@RequestMapping("${api.version}/auth")
@Tag(name = "Autenticación", description = "Endpoints para registro e inicio de sesión")
public class AuthRestController {

    private final AuthenticationService authenticationService;

    /**
     * Constructor del controlador.
     *
     * @param authenticationService Servicio responsable de manejar la lógica de autenticación.
     */
    @Autowired
    public AuthRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Endpoint para registrar un nuevo usuario.
     *
     * @param userSignUpRequest Datos de la solicitud de registro.
     * @return Respuesta con el token JWT generado para el nuevo usuario.
     */
    @PostMapping("/signup")
    public ResponseEntity<JwtAuthResponseDto> signup(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        log.info("[AUTH] Signup request: {}", userSignUpRequest);
        return ResponseEntity.ok(authenticationService.signUp(userSignUpRequest));
    }

    /**
     * Endpoint para iniciar sesión.
     *
     * @param userSignInRequest Datos de inicio de sesión del usuario.
     * @return Respuesta con el token JWT generado si las credenciales son válidas.
     */
    @PostMapping("/signin")
    public ResponseEntity<JwtAuthResponseDto> signin(@Valid @RequestBody UserSignInRequest userSignInRequest) {
        log.info("[AUTH] Signin request: {}", userSignInRequest);
        return ResponseEntity.ok(authenticationService.signIn(userSignInRequest));
    }

    /**
     * Maneja errores de validación y construye una respuesta clara con los campos y mensajes.
     *
     * @param ex Excepción generada por errores de validación.
     * @return Mapa con campos inválidos y sus respectivos mensajes.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
    /**
     * Maneja la excepción cuando un usuario intenta iniciar sesión pero su cuenta está desactivada.
     *
     * Esta excepción es capturada cuando se lanza {@link org.example.proyectoauth.rest.auth.exceptions.AuthSignInStatusException}
     * desde el servicio de autenticación. Devuelve un mensaje con código de estado HTTP 403 (Forbidden).
     *
     * @param ex Excepción lanzada al intentar autenticar un usuario desactivado.
     * @return Un mapa con el mensaje de error correspondiente.
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(org.example.proyectoauth.rest.auth.exceptions.AuthSignInStatusException.class)
    public Map<String, String> handleDisabledUserException(org.example.proyectoauth.rest.auth.exceptions.AuthSignInStatusException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }

    /**
     * Maneja la excepción cuando un usuario proporciona credenciales incorrectas al intentar iniciar sesión.
     *
     * Esta excepción es capturada cuando se lanza {@link org.example.proyectoauth.rest.auth.exceptions.AuthSignInInvalid}
     * desde el servicio de autenticación. Devuelve un mensaje con código de estado HTTP 401 (Unauthorized).
     *
     * @param ex Excepción lanzada por credenciales inválidas.
     * @return Un mapa con el mensaje de error correspondiente.
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(org.example.proyectoauth.rest.auth.exceptions.AuthSignInInvalid.class)
    public Map<String, String> handleInvalidCredentialsException(org.example.proyectoauth.rest.auth.exceptions.AuthSignInInvalid ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return error;
    }


}
