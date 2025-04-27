package org.example.proyectoauth.rest.users.mapper;

import org.example.proyectoauth.rest.users.dto.UserInfoResponseDto;
import org.example.proyectoauth.rest.users.dto.UserRequestDto;
import org.example.proyectoauth.rest.users.dto.UserResponseDto;
import org.example.proyectoauth.rest.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Clase encargada de mapear entre objetos relacionados con usuarios, como entidades {@link User}
 * y DTOs como {@link UserRequestDto}, {@link UserResponseDto} y {@link UserInfoResponseDto}.
 *
 * <p>Utiliza un {@link PasswordEncoder} para asegurar el almacenamiento seguro de contraseñas.</p>
 */
@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor de la clase UserMapper.
     *
     * @param passwordEncoder Codificador de contraseñas utilizado para cifrar contraseñas.
     */
    @Autowired
    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Convierte un {@link UserRequestDto} en una entidad {@link User}.
     * Se utiliza normalmente en la creación de un nuevo usuario.
     *
     * @param request Objeto con los datos del nuevo usuario.
     * @return Usuario construido con los datos proporcionados.
     */
    public User toUser(UserRequestDto request) {
        return User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .isActive(request.getIsActive())
                .build();
    }

    /**
     * Convierte un {@link User} existente y un {@link UserRequestDto} en una nueva instancia actualizada.
     *
     * @param user    Usuario original a actualizar.
     * @param request Datos actualizados del usuario.
     * @param id      Identificador único del usuario.
     * @return Objeto {@link User} actualizado.
     */
    public User toUser(User user, UserRequestDto request, Long id) {
        return User.builder()
                .id(id)
                .name(request.getName() == null ? user.getName() : request.getName())
                .username(request.getUsername() == null ? user.getUsername() : request.getUsername())
                .email(request.getEmail() == null ? user.getEmail() : request.getEmail())
                .password(
                        request.getPassword() == null
                                ? user.getPassword()  // ⚠️ ya está cifrada, no volver a cifrar
                                : passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles() == null ? user.getRoles() : request.getRoles())
                .isActive(request.getIsActive() == null ? user.getIsActive() : request.getIsActive())
                .build();
    }

    /**
     * Convierte un objeto {@link User} en un {@link UserResponseDto} para enviar al cliente.
     *
     * ⚠️ Nota: No se debería incluir la contraseña en este DTO. Ya has retirado la contraseña del DTO,
     * así que si este método se usa, debe eliminar el `password` o no asignarlo.
     *
     * @param user Entidad de usuario.
     * @return DTO con los datos del usuario.
     */
    public UserResponseDto toUserResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                // .password(...) eliminado por seguridad
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .build();
    }

    /**
     * Convierte un objeto {@link User} en un {@link UserInfoResponseDto}.
     * Este DTO se utiliza cuando se desea mostrar más información (como perfil).
     *
     * @param user Usuario a mapear.
     * @return DTO con la información extendida del usuario.
     */
    public UserInfoResponseDto toUserInfoResponse(User user) {
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isActive(user.getIsActive())
                .build();
    }
}
