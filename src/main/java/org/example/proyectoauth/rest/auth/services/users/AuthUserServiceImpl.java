package org.example.proyectoauth.rest.auth.services.users;

import org.example.proyectoauth.rest.auth.repositories.AuthRepository;
import org.example.proyectoauth.rest.users.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementación de {@link AuthUserService} que carga usuarios desde el repositorio de autenticación.
 *
 * Esta clase actúa como fuente principal de usuarios para Spring Security durante el proceso
 * de autenticación. Devuelve un {@link UserDetails} compatible con el sistema de seguridad.
 *
 * El nombre del servicio se define como {@code userDetailsService}, lo que permite su uso automático
 * por Spring Security cuando se requiere autenticación basada en nombre de usuario.
 */
@Service("userDetailsService")
public class AuthUserServiceImpl implements AuthUserService {

    private final AuthRepository authRepository;

    /**
     * Constructor con inyección del repositorio de usuarios.
     *
     * @param authRepository Repositorio que permite acceder a los usuarios.
     */
    @Autowired
    public AuthUserServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Carga un usuario desde la base de datos por su nombre de usuario.
     *
     * Este método es utilizado automáticamente por Spring Security para autenticar usuarios.
     *
     * @param username Nombre de usuario a buscar.
     * @return {@link UserDetails} si el usuario existe.
     * @throws UserNotFound si no se encuentra el usuario con el nombre especificado.
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("El usuario con nombre '" + username + "' no fue encontrado"));
    }
}
