package org.example.proyectoauth.rest.users.exceptions;

/**
 * Clase base abstracta para excepciones relacionadas con usuarios en la aplicación.
 *
 * <p>
 * Esta clase extiende {@link RuntimeException}, por lo que es una excepción no verificada (unchecked).
 * Sirve como superclase para todas las excepciones específicas relacionadas con la lógica de usuario,
 * como errores de búsqueda, validación, o condiciones de negocio.
 * </p>
 *
 * <p>
 * Todas las excepciones personalizadas de usuarios deben heredar de esta clase.
 * </p>
 */
public abstract class UserException extends RuntimeException {

    /**
     * Constructor que permite establecer un mensaje descriptivo para la excepción.
     *
     * @param message Mensaje que describe el motivo de la excepción.
     */
    public UserException(String message) {
        super(message);
    }
}
