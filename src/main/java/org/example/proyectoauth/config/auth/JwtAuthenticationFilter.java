package org.example.proyectoauth.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.proyectoauth.rest.auth.services.jwt.JwtService;
import org.example.proyectoauth.rest.auth.services.users.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de autenticación JWT que intercepta cada solicitud HTTP entrante.
 *
 * Se encarga de:
 * <ul>
 *     <li>Extraer el token JWT del encabezado Authorization.</li>
 *     <li>Validar el token y extraer el nombre de usuario.</li>
 *     <li>Autenticar al usuario si el token es válido.</li>
 *     <li>Establecer el contexto de seguridad para futuras operaciones.</li>
 * </ul>
 *
 * Este filtro se ejecuta una sola vez por solicitud, gracias a que extiende {@link OncePerRequestFilter}.
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthUserService authUserService;

    @Value("${api.version}")
    private String apiVersion;

    /**
     * Constructor que inyecta los servicios necesarios para manejar la lógica JWT y
     * cargar información de los usuarios autenticados.
     *
     * @param jwtService      Servicio encargado de operaciones sobre tokens JWT.
     * @param authUserService Servicio para recuperar detalles de los usuarios.
     */
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, AuthUserService authUserService) {
        this.jwtService = jwtService;
        this.authUserService = authUserService;
    }

    /**
     * Lógica principal del filtro. Este método se ejecuta en cada petición HTTP
     * para verificar si incluye un token JWT válido y, si es así, autenticar al usuario.
     *
     * @param request     Solicitud HTTP entrante.
     * @param response    Respuesta HTTP saliente.
     * @param filterChain Cadena de filtros que siguen tras este.
     * @throws ServletException En caso de fallo interno del servlet.
     * @throws IOException      En caso de error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("[JWT Filter] → Iniciando filtro de autenticación para: {}", request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        UserDetails userDetails;
        String username;

        // Verifica que el header exista y esté en formato correcto
        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
            log.debug("[JWT Filter] → No se encontró header Authorization válido. Continuando sin autenticación.");
            filterChain.doFilter(request, response);
            return;
        }

        // Extrae el token JWT eliminando el prefijo "Bearer "
        jwt = authHeader.substring(7);
        log.debug("[JWT Filter] → Token JWT recibido: {}", jwt);

        try {
            // Extrae el nombre de usuario del token
            username = jwtService.extractUserName(jwt);
            log.debug("[JWT Filter] → Nombre de usuario extraído del token: {}", username);
        } catch (Exception e) {
            log.warn("[JWT Filter] → Token inválido o malformado.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token JWT inválido");
            return;
        }

        // Si no hay un usuario autenticado en el contexto actual
        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                userDetails = authUserService.loadUserByUsername(username);
                log.debug("[JWT Filter] → Usuario encontrado en sistema: {}", username);
            } catch (Exception e) {
                log.warn("[JWT Filter] → Usuario no autorizado o no existe: {}", username);
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autorizado");
                return;
            }

            // Valida el token con los datos del usuario
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("[JWT Filter] → Token válido. Autenticando usuario: {}", username);

                // Crea un contexto de seguridad limpio
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                // Construye el token de autenticación
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Añade detalles del request al token (por ejemplo IP, navegador, etc.)
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Asigna el contexto autenticado
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);

                log.info("[JWT Filter] → Contexto de seguridad establecido para el usuario: {}", username);
            } else {
                log.warn("[JWT Filter] → Token no válido para el usuario: {}", username);
            }
        } else {
            log.debug("[JWT Filter] → Ya existe un usuario autenticado o username vacío.");
        }

        // Continúa la ejecución de la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Determina si este filtro debe o no ejecutarse para la solicitud actual.
     *
     * Se utiliza para evitar aplicar el filtro a rutas públicas de autenticación
     * como signin o signup (que no requieren autenticación previa).
     *
     * @param request La solicitud HTTP actual.
     * @return true si el filtro debe ser omitido para esta solicitud.
     * @throws ServletException En caso de error en la ejecución.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        boolean exclude = path.startsWith("/" + apiVersion + "/auth/");
        if (exclude) {
            log.debug("[JWT Filter] → Saltando filtro para ruta pública: {}", path);
        }
        return exclude;
    }
}
