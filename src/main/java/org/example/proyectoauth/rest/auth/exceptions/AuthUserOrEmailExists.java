package org.example.proyectoauth.rest.auth.exceptions;

/**
 * Excepción lanzada cuando se intenta registrar un usuario o correo electrónico
 * que ya existe en el sistema.
 *
 * Esta excepción es útil para validar unicidad durante el proceso de registro.
 */
public class AuthUserOrEmailExists extends AuthException {

    /**
     * Crea una nueva instancia de AuthUserOrEmailExists con el mensaje especificado.
     *
     * @param message Mensaje descriptivo del error de duplicidad.
     */
    public AuthUserOrEmailExists(String message) {
        super(message);
    }
}
