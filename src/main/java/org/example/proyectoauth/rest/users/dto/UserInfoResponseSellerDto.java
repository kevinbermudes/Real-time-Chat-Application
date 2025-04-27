package org.example.proyectoauth.rest.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.proyectoauth.rest.users.model.Role;

import java.util.Set;

/**
 * DTO que representa información detallada del usuario desde la perspectiva de gestión de vendedores.
 *
 * <p>Incluye datos esenciales del usuario como identificador, nombre, username, correo,
 * roles y estado de activación. Pensado para ser utilizado por perfiles administradores o sistemas de gestión.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponseSellerDto {

    /**
     * Identificador único del usuario.
     */
    private Long id;

    /**
     * Nombre completo del usuario.
     */
    private String name;

    /**
     * Nombre de usuario utilizado para autenticación.
     */
    private String username;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Conjunto de roles asignados al usuario.
     * Por defecto se asigna ROLE.USER.
     */
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

    /**
     * Indica si el usuario está actualmente activo en el sistema.
     */
    @Builder.Default
    private Boolean isActive = false;
}
