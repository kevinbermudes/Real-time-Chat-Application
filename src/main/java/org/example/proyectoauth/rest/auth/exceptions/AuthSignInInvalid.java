package org.example.proyectoauth.rest.auth.exceptions;

/**
 * Excepción lanzada cuando ocurre un intento de inicio de sesión no válido.
 *
 * Esta excepción puede representar casos como:
 * - Usuario no encontrado.
 * - Contraseña incorrecta.
 * - Usuario inhabilitado o bloqueado.
 *
 * Se utiliza para separar los errores de autenticación de otras excepciones genéricas.
 */
public class AuthSignInInvalid extends AuthException {

    /**
     * Crea una nueva instancia de AuthSignInInvalid con el mensaje proporcionado.
     *
     * @param message Mensaje descriptivo del error de autenticación.
     */
    public AuthSignInInvalid(String message) {
        super(message);
    }
}
