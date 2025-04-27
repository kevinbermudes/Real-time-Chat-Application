package org.example.proyectoauth.WebSockets.model;

import lombok.Builder;

/**
 * Representación inmutable de una notificación emitida por el sistema.
 *
 * @param <T> Tipo del contenido de la notificación (puede ser una entidad, un ID, un DTO, etc.).
 */
@Builder
public record Notificacion<T>(
        String entity,
        Tipo tipo,
        T data,
        String createdAt
) {
    /**
     * Tipos posibles de eventos que pueden generar una notificación.
     *
     * - CREATE: se creó un nuevo recurso.
     * - UPDATE: se modificó un recurso existente.
     * - DELETE: se eliminó un recurso.
     */
    public enum Tipo {
        CREATE,
        UPDATE,
        DELETE
    }
}
