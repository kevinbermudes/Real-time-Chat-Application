package org.example.proyectoauth.WebSockets.exceptions;

/**
 * Clase base abstracta para excepciones relacionadas con notificaciones en tiempo real vía WebSocket.
 *
 * Esta excepción se utiliza como superclase para todos los errores que puedan ocurrir
 * durante el envío, recepción o procesamiento de mensajes WebSocket.
 *
 * Al extender {@link RuntimeException}, no requiere ser declarada ni capturada explícitamente.
 */
public abstract class NotificacionException extends RuntimeException {

    /**
     * Crea una nueva instancia de excepción de notificación.
     *
     * @param message Descripción del error.
     */
    public NotificacionException(String message) {
        super(message);
    }
}
