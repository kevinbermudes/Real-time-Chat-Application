package org.example.proyectoauth.rest.users.model;

/**
 * Enumeración que representa los distintos roles que puede tener un usuario en el sistema.
 *
 * <p>Cada rol define el nivel de permisos y acceso a funcionalidades dentro de la aplicación.</p>
 *
 * <ul>
 *     <li>{@code USER} – Rol básico asignado a los usuarios comunes.</li>
 *     <li>{@code ADMIN} – Rol con privilegios elevados para administración y gestión completa del sistema.</li>
 * </ul>
 */
public enum Role {

    /**
     * Rol básico para usuarios finales del sistema. Permite acceso a funcionalidades estándar.
     */
    USER,

    /**
     * Rol de administrador con acceso a funcionalidades restringidas y capacidad de gestión de otros usuarios.
     */
    ADMIN
}
