package org.example.proyectoauth.config.websockets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.SubProtocolCapable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Manejador WebSocket personalizado que gestiona las conexiones entrantes,
 * envía mensajes a los clientes conectados y permite emitir mensajes periódicos.
 *
 * Este manejador está diseñado para ser reutilizado en distintos módulos y proyectos,
 * enviando mensajes relacionados con la entidad definida en su constructor.
 */
@Slf4j
public class WebSocketHandler extends TextWebSocketHandler implements SubProtocolCapable, WebSocketSender {

    // Nombre de la entidad asociada al canal (útil para logs)
    private final String entity;

    // Lista segura para múltiples hilos de sesiones WebSocket activas
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();

    /**
     * Constructor que define el nombre de la entidad para este canal WebSocket.
     *
     * @param entity Nombre de la entidad asociada (por ejemplo: "Notificación").
     */
    public WebSocketHandler(String entity) {
        this.entity = entity;
    }

    /**
     * Se ejecuta cuando se establece una nueva conexión WebSocket.
     *
     * @param session La nueva sesión WebSocket.
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("[WS] Conexión establecida. Sesión: {}", session.getId());
        sessions.add(session);

        try {
            // Mensaje de bienvenida opcional
            TextMessage message = new TextMessage("Conectado al WebSocket de: " + entity);
            session.sendMessage(message);
            log.info("[WS] Mensaje enviado al cliente: {}", message.getPayload());
        } catch (IOException e) {
            log.error("[WS] Error al enviar mensaje inicial: {}", e.getMessage());
        }
    }

    /**
     * Se ejecuta cuando una conexión WebSocket es cerrada.
     *
     * @param session La sesión cerrada.
     * @param status  El estado de cierre.
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("[WS] Sesión cerrada: {} - Estado: {}", session.getId(), status);
    }

    /**
     * Envía un mensaje de texto a todos los clientes conectados.
     *
     * @param message El mensaje a enviar.
     * @throws IOException Si ocurre un error al enviar el mensaje.
     */
    @Override
    public void sendMessage(String message) throws IOException {
        log.info("[WS] Enviando mensaje para la entidad '{}': {}", entity, message);
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }

    /**
     * Envía un mensaje automático cada segundo a los clientes conectados.
     * Ideal para pruebas o mantener conexiones activas.
     *
     * @param message Contenido del mensaje a enviar.
     * @throws IOException Si ocurre un error al enviar el mensaje.
     */
    @Scheduled(fixedRate = 1000)
    @Override
    public void sendPeriodicMessage(String message) throws IOException {
        String periodic = "Mensaje periódico del servidor a las " + LocalTime.now();
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(periodic));
                log.info("[WS] Periodic: {}", periodic);
            }
        }
    }

    /**
     * Maneja los mensajes recibidos desde el cliente.
     * En este caso, no se responde a ningún mensaje (modo solo emisión).
     *
     * @param session Sesión desde la que se recibió el mensaje.
     * @param message El mensaje recibido.
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Modo solo-emisor: no se procesan mensajes entrantes.
        log.debug("[WS] Mensaje recibido (no procesado): {}", message.getPayload());
    }

    /**
     * Maneja errores de transporte durante una sesión WebSocket.
     *
     * @param session   La sesión donde ocurrió el error.
     * @param exception La excepción lanzada.
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.warn("[WS] Error en la sesión {}: {}", session.getId(), exception.getMessage());
    }

    /**
     * Define los subprotocolos soportados por este canal WebSocket.
     *
     * @return Lista de subprotocolos compatibles.
     */
    @Override
    public List<String> getSubProtocols() {
        return List.of("subprotocol.demo.websocket");
    }
}
