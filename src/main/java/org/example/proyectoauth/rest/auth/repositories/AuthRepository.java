package org.example.proyectoauth.rest.auth.repositories;

import org.example.proyectoauth.rest.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para operaciones de autenticación de usuarios.
 *
 * Proporciona métodos para consultar y gestionar entidades {@link User} en la base de datos.
 */
@Repository
public interface AuthRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username Nombre de usuario a buscar.
     * @return {@link Optional} que contiene el usuario si existe, o vacío si no se encuentra.
     */
    Optional<User> findByUsername(String username);
    /**
     * Busca un usuario por su correo electrónico.
     *
     * @param email Correo electrónico a buscar.
     * @return {@link Optional} que contiene el usuario si existe, o vacío si no se encuentra.
     */
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

}
