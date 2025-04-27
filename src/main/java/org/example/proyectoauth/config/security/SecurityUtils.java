package org.example.proyectoauth.config.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Clase utilitaria para obtener información del usuario autenticado en el contexto de seguridad de Spring.
 *
 * Esta clase proporciona métodos estáticos para acceder fácilmente al nombre de usuario actual,
 * lo cual es útil para registrar actividad, auditoría o personalización de lógica de negocio.
 */
public class SecurityUtils {

    /**
     * Obtiene el nombre de usuario del usuario actualmente autenticado.
     *
     * @return El nombre de usuario si está autenticado; en caso contrario, retorna "ANÓNIMO".
     */
    public static String getCurrentUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        return "ANÓNIMO";
    }
}
