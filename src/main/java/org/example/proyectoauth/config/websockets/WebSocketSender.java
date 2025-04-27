package org.example.proyectoauth.config.websockets;

import java.io.IOException;

/**
 * Interfaz funcional para el envío de mensajes a través de WebSocket.
 *
 * Define dos métodos:
 * - Uno para enviar mensajes únicos.
 * - Otro para enviar mensajes periódicos (por ejemplo, desde un scheduler).
 *
 * Esta interfaz permite desacoplar el envío de mensajes del manejador,
 * facilitando su reutilización o inyección desde otros componentes.
 */
public interface WebSocketSender {

    /**
     * Envía un mensaje de texto a todos los clientes conectados por WebSocket.
     *
     * @param message El mensaje a enviar.
     * @throws IOException Si ocurre un error al enviar el mensaje.
     */
    void sendMessage(String message) throws IOException;

    /**
     * Envía un mensaje periódico a los clientes WebSocket.
     * Este método puede ser utilizado con anotaciones como {@code @Scheduled}.
     *
     * @param message El mensaje periódico a enviar.
     * @throws IOException Si ocurre un error durante el envío.
     */
    void sendPeriodicMessage(String message) throws IOException;
}
