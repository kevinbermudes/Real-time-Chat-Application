package org.example.proyectoauth.rest.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.proyectoauth.rest.users.model.Role;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

/**
 * DTO (Data Transfer Object) que representa la información necesaria para crear o actualizar un usuario.
 *
 * <p>
 * Este DTO se utiliza para operaciones de entrada (peticiones HTTP), como la creación de nuevos usuarios
 * o la modificación de sus datos desde el lado del administrador o del propio usuario.
 * </p>
 *
 * <p>Incluye validaciones usando anotaciones de Jakarta Bean Validation.</p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    /**
     * Nombre completo del usuario.
     */
    @NotBlank(message = "Name cannot be empty")
    private String name;

    /**
     * Nombre de usuario único.
     */
    @NotBlank(message = "Username cannot be empty")
    private String username;

    /**
     * Correo electrónico válido del usuario.
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    private String email;

    /**
     * Contraseña del usuario. Debe tener al menos 8 caracteres.
     */
    @NotBlank(message = "Password cannot be empty")
    @Length(min = 8, message = "Password debe tener al menos 8 caracteres")
    private String password;

    /**
     * Conjunto de roles asignados al usuario.
     * Por defecto se establece ROLE.USER.
     */
    @NotNull(message = "Role cannot be empty")
    @Builder.Default
    private Set<Role> roles = Set.of(Role.USER);

    /**
     * Indica si el usuario está activo en el sistema.
     */
    @Builder.Default
    private Boolean isActive = false;
}
