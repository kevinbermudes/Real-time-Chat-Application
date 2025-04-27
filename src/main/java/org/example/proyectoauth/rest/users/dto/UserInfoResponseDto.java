package org.example.proyectoauth.rest.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.proyectoauth.rest.users.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * DTO (Data Transfer Object) que representa la información detallada de un usuario.
 *
 * <p>Este DTO se utiliza principalmente en respuestas cuando se desea mostrar el perfil completo
 * o los detalles de un usuario, incluyendo roles, estado y referencias relacionadas (como pedidos).</p>
 *
 * <p>Utiliza anotaciones de Lombok para reducir el boilerplate (constructores, getters/setters, etc.).</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseDto {

    /**
     * Identificador único del usuario.
     */
    private Long id;

    /**
     * Nombre completo del usuario.
     */
    private String name;

    /**
     * Nombre de usuario utilizado para iniciar sesión.
     */
    private String username;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Conjunto de roles asignados al usuario.
     * Por defecto contiene el rol USER.
     */
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

    /**
     * Indica si el usuario está activo o no.
     */
    @Builder.Default
    private Boolean isActive = false;

}
