package org.example.proyectoauth.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada que se lanza cuando se intenta realizar una operación
 * con un usuario que no existe en el sistema.
 *
 * <p>
 * Esta excepción está anotada con {@code @ResponseStatus(HttpStatus.NOT_FOUND)},
 * por lo que al lanzarse devolverá automáticamente una respuesta HTTP 404 (Not Found).
 * </p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends UserException {

    /**
     * Constructor que permite establecer un mensaje descriptivo sobre el usuario no encontrado.
     *
     * @param message Detalles adicionales sobre la búsqueda fallida del usuario.
     */
    public UserNotFound(String message) {
        super("Usuario con " + message + " no encontrado");
    }
}
