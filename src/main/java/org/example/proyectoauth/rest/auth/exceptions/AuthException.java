package org.example.proyectoauth.rest.auth.exceptions;

/**
 * Clase base abstracta para excepciones relacionadas con la autenticación.
 *
 * Esta clase extiende {@link RuntimeException} y actúa como superclase
 * para todas las excepciones personalizadas dentro del contexto de autenticación.
 *
 * Su uso permite un manejo centralizado de errores específicos del proceso de autenticación.
 */
public abstract class AuthException extends RuntimeException {

    /**
     * Crea una nueva instancia de AuthException con el mensaje proporcionado.
     *
     * @param message Mensaje descriptivo de la excepción.
     */
    public AuthException(String message) {
        super(message);
    }
}
