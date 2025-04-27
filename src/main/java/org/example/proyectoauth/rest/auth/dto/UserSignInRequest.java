package org.example.proyectoauth.rest.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa la solicitud de inicio de sesión de un usuario.
 *
 * Contiene las credenciales mínimas requeridas para autenticar al usuario
 * mediante el sistema de autenticación (por ejemplo, JWT).
 *
 * Las validaciones aseguran que los campos no estén vacíos.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInRequest {

    /**
     * Nombre de usuario utilizado para iniciar sesión.
     * Este campo no puede estar en blanco.
     */
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    /**
     * Contraseña asociada al usuario.
     * Este campo no puede estar en blanco.
     */
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}
