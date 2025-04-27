package org.example.proyectoauth.rest.users.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Entidad que representa un usuario en el sistema.
 *
 * <p>
 * Esta clase implementa la interfaz {@link UserDetails} para integrarse con el sistema de autenticación
 * de Spring Security.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "USERS")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "users_seq", allocationSize = 1)
    private Long id;


    /**
     * Nombre completo del usuario.
     */
    @NotBlank(message = "El nombre no puede estar vacío")
    @Column(nullable = false)
    private String name;

    /**
     * Nombre de usuario único. Se utiliza para iniciar sesión.
     */
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * Dirección de correo electrónico única del usuario.
     */
    @NotBlank(message = "El correo electrónico no puede estar vacío")
    @Email(regexp = ".*@.*\\..*", message = "El correo debe tener un formato válido")
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Contraseña cifrada del usuario.
     */
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Length(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Column(nullable = false)
    private String password;

    /**
     * Fecha y hora de creación del usuario.
     */
    @Builder.Default
    @CreationTimestamp
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Fecha y hora de la última actualización del usuario.
     */
    @Builder.Default
    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Indica si el usuario está activo. Por defecto: true.
     */
    @Builder.Default
    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;

    /**
     * Conjunto de roles asignados al usuario.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    // Métodos de la interfaz UserDetails

    /**
     * Devuelve las autoridades (roles) asignadas al usuario.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    /**
     * Devuelve el nombre de usuario (username) usado para autenticación.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indica si la cuenta no ha expirado.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indica si la cuenta no está bloqueada.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indica si las credenciales no han expirado.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indica si el usuario está habilitado en el sistema.
     */
//    @Override
//    public boolean isEnabled() {
//        return true; // Si quieres usar `isActive`, puedes devolver: return Boolean.TRUE.equals(isActive);
//    }
    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(isActive);
    }

}
