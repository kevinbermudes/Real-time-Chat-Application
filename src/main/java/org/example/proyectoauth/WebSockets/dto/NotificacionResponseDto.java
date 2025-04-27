package org.example.proyectoauth.WebSockets.dto;

import lombok.Builder;

/**
 * DTO de respuesta utilizado para enviar notificaciones a través de WebSocket.
 *
 * Esta clase inmutable contiene la entidad afectada y los datos relevantes del cambio.
 *
 * @param entity Nombre o tipo de la entidad asociada al evento (por ejemplo: "producto", "pedido").
 * @param data   Datos adicionales del evento o cambio (puede ser un ID, mensaje, JSON, etc.).
 */
@Builder
public record NotificacionResponseDto(
        String entity,
        String data
) {
}
