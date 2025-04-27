package org.example.proyectoauth.config.websockets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuración para habilitar y registrar canales WebSocket en la aplicación.
 *
 * Esta clase permite definir dinámicamente:
 * - La URL del canal WebSocket.
 * - La entidad asociada para su manejo.
 *
 * Los valores se cargan automáticamente desde el archivo de configuración según el perfil activo.
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * URL del canal WebSocket. Se define en application.properties como:
     * websocket.url=notificaciones
     */
    @Value("${websocket.url}")
    private String urlWebSocket;

    /**
     * Nombre de la entidad asociada al canal WebSocket. Se define como:
     * websocket.entity=Notificacion
     */
    @Value("${websocket.entity}")
    private String entity;

    /**
     * Registra el canal WebSocket en la ruta especificada.
     * Permite cualquier origen (solo recomendable para desarrollo).
     *
     * @param registry Registro de manejadores WebSocket.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketHandler(), "/ws/" + urlWebSocket)
                .setAllowedOrigins("*"); // En producción deberías restringirlo a tus dominios
    }

    /**
     * Bean encargado de manejar la lógica del canal WebSocket.
     *
     * @return Handler asociado a la entidad.
     */
    @Bean
    public WebSocketHandler webSocketHandler() {
        return new WebSocketHandler(entity);
    }
}
