package org.example.proyectoauth.rest.auth.services.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Interfaz personalizada para el servicio de autenticación de usuarios.
 *
 * Extiende {@link UserDetailsService} de Spring Security y permite implementar
 * lógica específica para la carga de usuarios desde una fuente de datos.
 */
public interface AuthUserService extends UserDetailsService {

    /**
     * Carga los detalles de un usuario a partir de su nombre de usuario.
     *
     * @param username Nombre de usuario a buscar.
     * @return {@link UserDetails} del usuario encontrado.
     */
    @Override
    UserDetails loadUserByUsername(String username);
}
