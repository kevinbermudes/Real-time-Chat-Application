package org.example.proyectoauth.rest.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.proyectoauth.rest.users.model.Role;

import java.util.Set;

/**
 * DTO que representa los datos del usuario que se devuelven al cliente.
 *
 * Esta clase encapsula información pública del usuario autenticado o consultado.
 * Se utiliza comúnmente en endpoints que devuelven datos de perfil o administración.
 *
 * Lombok genera automáticamente:
 * - Getters y setters (@Data)
 * - Constructores (@NoArgsConstructor, @AllArgsConstructor)
 * - Builder fluido (@Builder)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /**
     * Identificador único del usuario.
     */
    private Long id;

    /**
     * Nombre completo del usuario.
     */
    private String name;

    /**
     * Nombre de usuario (único) utilizado para iniciar sesión.
     */
    private String username;

    /**
     * Correo electrónico del usuario.
     */
    private String email;

    /**
     * Conjunto de roles asociados al usuario.
     * Este campo puede utilizarse para controlar los permisos y accesos.
     */
    private Set<Role> roles;
}
