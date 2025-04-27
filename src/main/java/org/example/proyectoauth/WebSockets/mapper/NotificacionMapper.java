package org.example.proyectoauth.WebSockets.mapper;

import org.example.proyectoauth.WebSockets.dto.NotificacionResponseDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Mapper genérico para convertir cualquier objeto en un DTO de notificación WebSocket.
 *
 * @param <T> Tipo del objeto de dominio a mapear.
 */
@Component
public class NotificacionMapper<T> {

    /**
     * Convierte un objeto y una entidad en un {@link NotificacionResponseDto}.
     *
     * @param t      Objeto que representa el contenido de la notificación.
     * @param entity Nombre de la entidad relacionada (por ejemplo, "producto", "pedido").
     * @return DTO de notificación que incluye la entidad y el contenido textual del objeto.
     */
    public NotificacionResponseDto getNotificacionResponseDto(T t, String entity) {
        return new NotificacionResponseDto(
                entity,
                Objects.toString(t) // previene NPE si t == null
        );
    }
}
