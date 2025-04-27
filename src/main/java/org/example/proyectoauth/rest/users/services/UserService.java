package org.example.proyectoauth.rest.users.services;

import org.example.proyectoauth.rest.users.dto.UserInfoResponseDto;
import org.example.proyectoauth.rest.users.dto.UserProfileUpdateDto;
import org.example.proyectoauth.rest.users.dto.UserRequestDto;
import org.example.proyectoauth.rest.users.dto.UserResponseDto;
import org.example.proyectoauth.rest.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interfaz que define las operaciones del servicio para gestionar usuarios.
 *
 * <p>Contiene métodos para consultar, registrar, actualizar y eliminar usuarios del sistema.</p>
 */
public interface UserService {

    /**
     * Obtiene una lista paginada de usuarios, con filtros opcionales por nombre de usuario, email y estado de activación.
     *
     * @param username Nombre de usuario opcional para filtrar (parcial o completo).
     * @param email    Email opcional para filtrar.
     * @param isActive Estado de activación opcional para filtrar (true o false).
     * @param pageable Objeto de configuración de paginación y orden.
     * @return Página con los usuarios que cumplen los criterios de búsqueda.
     */
    Page<UserResponseDto> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isActive, Pageable pageable);

    /**
     * Busca y devuelve los detalles completos de un usuario a partir de su ID.
     *
     * @param id ID único del usuario.
     * @return DTO con la información detallada del usuario.
     */
    UserInfoResponseDto findById(Long id);

    /**
     * Busca un usuario por su nombre de usuario exacto.
     *
     * @param username Nombre de usuario a buscar.
     * @return {@link Optional} que contiene el usuario si existe, o vacío si no se encuentra.
     */
    Optional<User> findByUsername(String username);

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param userRequestDto Datos del nuevo usuario a registrar.
     * @return DTO con la información del usuario registrado.
     */
    UserResponseDto save(UserRequestDto userRequestDto);

    /**
     * Actualiza los datos de un usuario existente por su ID.
     *
     * @param id             ID del usuario a actualizar.
     * @param userRequestDto Nuevos datos del usuario.
     * @return DTO con la información del usuario actualizado.
     */
    UserResponseDto update(Long id, UserRequestDto userRequestDto);

    /**
     * Elimina lógicamente (o físicamente) un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     */
    void deleteById(Long id);
    /**
     * Actualiza el perfil del usuario autenticado sin permitir modificar roles ni estado.
     *
     * @param id ID del usuario.
     * @param dto DTO con los campos permitidos para editar su perfil.
     * @return DTO con la información actualizada del usuario.
     */
    UserResponseDto updateProfile(Long id, UserProfileUpdateDto dto);
}
