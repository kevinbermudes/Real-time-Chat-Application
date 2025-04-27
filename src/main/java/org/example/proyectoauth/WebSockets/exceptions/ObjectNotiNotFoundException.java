package org.example.proyectoauth.WebSockets.exceptions;

/**
 * Excepción lanzada cuando un objeto requerido para una notificación no ha sido encontrado.
 *
 * Esta excepción se utiliza, por ejemplo, cuando se intenta emitir una notificación
 * sobre una entidad que ya no existe o no es accesible en el sistema.
 */
public class ObjectNotiNotFoundException extends NotificacionException {

    /**
     * Crea una nueva excepción indicando que el objeto a notificar no fue encontrado.
     *
     * @param message Mensaje descriptivo del error.
     */
    public ObjectNotiNotFoundException(String message) {
        super(message);
    }
}
