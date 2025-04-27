package org.example.proyectoauth.rest.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileUpdateDto {

    @NotBlank(message = "Name cannot be empty")
    private String name;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(regexp = ".*@.*\\..*", message = "Email debe ser válido")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Length(min = 8, message = "Password debe tener al menos 8 caracteres")
    private String password;
}