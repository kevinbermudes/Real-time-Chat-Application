package org.example.proyectoauth.rest.auth.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Excepción con código de estado HTTP personalizable.
 */
public class AuthSignInStatusException extends AuthException {
    private final HttpStatus status;

    public AuthSignInStatusException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}