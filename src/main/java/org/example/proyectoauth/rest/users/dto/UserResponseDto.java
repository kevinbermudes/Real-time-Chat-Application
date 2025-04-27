package org.example.proyectoauth.rest.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.proyectoauth.rest.users.model.Role;

import java.util.Set;

/**
 * DTO (Data Transfer Object) que representa la información devuelta en respuestas relacionadas con usuarios.
 *
 * <p>Este DTO se utiliza principalmente para mostrar datos de usuario al frontend, tras operaciones como
 * creación, consulta o actualización.</p>
 *
 * <p>
 * ⚠️ Importante: Incluir la contraseña en este DTO **no es recomendable** por motivos de seguridad,
 * ya que podría ser accidentalmente expuesta en respuestas. Considera removerla si no es estrictamente necesaria.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    /**
     * Identificador único del usuario.
     */
    private Long id;

    /**
     * Nombre completo del usuario.
     */
    private String name;

    /**
     * Nombre de usuario único para autenticación.
     */
    private String username;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Roles asignados al usuario. Por defecto se establece ROLE.USER.
     */
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

    /**
     * Indica si el usuario está actualmente activo.
     */
    @Builder.Default
    private Boolean isActive = false;
}
