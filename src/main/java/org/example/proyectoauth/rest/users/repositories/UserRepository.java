package org.example.proyectoauth.rest.users.repositories;

import org.example.proyectoauth.rest.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de la entidad {@link User}.
 *
 * <p>Proporciona métodos personalizados para consultas específicas además de los métodos
 * estándar de {@link JpaRepository} y soporte para criterios dinámicos con {@link JpaSpecificationExecutor}.</p>
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Busca un usuario por su nombre de usuario, sin distinguir entre mayúsculas y minúsculas.
     *
     * @param username Nombre de usuario a buscar.
     * @return Un {@link Optional} con el usuario encontrado, o vacío si no existe.
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    /**
     * Busca un usuario por su correo electrónico exacto.
     *
     * @param email Correo electrónico a buscar.
     * @return Un {@link Optional} con el usuario encontrado, o vacío si no existe.
     */
    Optional<User> findByEmail(String email);

    /**
     * Busca un usuario por su nombre de usuario o correo electrónico, sin distinguir entre mayúsculas y minúsculas.
     *
     * @param username Nombre de usuario.
     * @param email    Correo electrónico.
     * @return Un {@link Optional} con el usuario encontrado, o vacío si no existe.
     */
    Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username, String email);

    /**
     * Busca todos los usuarios cuyo nombre de usuario coincida con el valor proporcionado,
     * sin distinguir entre mayúsculas y minúsculas.
     *
     * @param username Nombre de usuario a buscar.
     * @return Lista de usuarios encontrados.
     */
    List<User> findAllByUsernameIgnoreCase(String username);

    /**
     * Marca como inactivo a un usuario estableciendo su campo {@code isActive} en {@code false}.
     *
     * @param id ID del usuario a desactivar.
     */
    @Modifying
    @Query("UPDATE User p SET p.isActive = false WHERE p.id = :id")
    void updateIsActiveToFalseById(Long id);
}
