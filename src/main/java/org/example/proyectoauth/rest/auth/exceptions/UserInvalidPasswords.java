package org.example.proyectoauth.rest.auth.exceptions;

/**
 * Excepción lanzada cuando las contraseñas proporcionadas por el usuario
 * son inválidas o no coinciden entre sí.
 *
 * Comúnmente usada durante el registro o cambio de contraseña.
 */
public class UserInvalidPasswords extends AuthException {

    /**
     * Crea una nueva instancia de UserInvalidPasswords con el mensaje proporcionado.
     *
     * @param message Mensaje descriptivo del error relacionado con las contraseñas.
     */
    public UserInvalidPasswords(String message) {
        super(message);
    }
}
