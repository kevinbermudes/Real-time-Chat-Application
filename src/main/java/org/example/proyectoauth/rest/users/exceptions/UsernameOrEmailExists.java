package org.example.proyectoauth.rest.users.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada que se lanza cuando se intenta registrar o actualizar un usuario
 * con un nombre de usuario o correo electrónico que ya existen en el sistema.
 *
 * <p>
 * Esta excepción está anotada con {@code @ResponseStatus(HttpStatus.BAD_REQUEST)},
 * por lo que al lanzarse devolverá automáticamente una respuesta HTTP 400 (Bad Request).
 * </p>
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UsernameOrEmailExists extends UserException {

    /**
     * Constructor que permite establecer un mensaje descriptivo sobre el conflicto de duplicidad.
     *
     * @param message Detalles sobre el nombre de usuario o correo electrónico duplicado.
     */
    public UsernameOrEmailExists(String message) {
        super("Duplicado con los siguiente datos: '" + message + "' ya existe en el sistema.");
    }
}
