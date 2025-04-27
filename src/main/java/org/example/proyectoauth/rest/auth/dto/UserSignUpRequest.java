package org.example.proyectoauth.rest.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * DTO que representa la solicitud de registro de un nuevo usuario.
 *
 * Incluye los datos básicos requeridos para crear una cuenta en el sistema:
 * nombre, nombre de usuario, correo electrónico y dos campos de contraseña.
 *
 * ⚠️ Por razones de seguridad, esta clase no debe registrarse en logs en producción.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpRequest {

    /**
     * Nombre completo del usuario.
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;

    /**
     * Nombre de usuario único para iniciar sesión.
     */
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;

    /**
     * Correo electrónico del usuario.
     * Debe tener un formato válido.
     */
    @Email(regexp = ".*@.*\\..*", message = "El correo electrónico debe ser válido")
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    private String email;

    /**
     * Contraseña del usuario.
     * Debe tener al menos 8 caracteres.
     */
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Length(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    /**
     * Campo de confirmación de contraseña.
     * Debe coincidir con la contraseña original.
     */
    @NotBlank(message = "La confirmación de contraseña no puede estar vacía")
    @Length(min = 8, message = "La confirmación de contraseña debe tener al menos 8 caracteres")
    private String passwordComprobacion;
}
