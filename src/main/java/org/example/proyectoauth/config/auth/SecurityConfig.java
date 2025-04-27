package org.example.proyectoauth.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Clase principal de configuración de seguridad para la aplicación.
 *
 * Define cómo se gestionan las sesiones, la autenticación y la autorización
 * de las peticiones HTTP usando Spring Security.
 *
 * Habilita la seguridad a nivel de métodos mediante {@link EnableMethodSecurity},
 * lo que permite usar anotaciones como @PreAuthorize en los controladores.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // Servicio que carga detalles de usuarios (implementación de UserDetailsService)
    private final UserDetailsService userService;

    // Filtro JWT personalizado que valida el token en cada solicitud
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Versión del API cargada desde application.properties
    @Value("${api.version}")
    private String apiVersion;

    /**
     * Constructor para inyectar dependencias requeridas por la configuración de seguridad.
     *
     * @param userService            Servicio que proporciona los detalles de autenticación de usuarios.
     * @param jwtAuthenticationFilter Filtro personalizado para validar tokens JWT.
     */
    @Autowired
    public SecurityConfig(UserDetailsService userService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Define la cadena de filtros de seguridad y configura las reglas de acceso.
     *
     * - Desactiva CSRF (porque se usa JWT, no cookies).
     * - Configura la política de sesiones como sin estado (STATLESS).
     * - Permite el acceso a rutas públicas sin autenticación.
     * - Aplica el filtro JWT antes del filtro de autenticación por usuario y contraseña.
     *
     * @param http Objeto HttpSecurity para personalizar la configuración de seguridad.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception En caso de error en la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println(">>> Valor de apiVersion: " + apiVersion); // útil para debug

        http
                // Desactiva protección CSRF porque usamos JWT, no sesiones tradicionales
                .csrf(AbstractHttpConfigurer::disable)

                // Permite cargar el H2 console sin restricciones de frame
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))

                // Define que no se deben crear sesiones; cada request debe tener su propio JWT
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define reglas de acceso
                .authorizeHttpRequests(auth -> auth
                        // Endpoints públicos y de documentación, sin autenticación
                        .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/error/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/static/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/ws/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/storage/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/" + apiVersion + "/auth/**")).permitAll()

                        // Todo lo demás requiere autenticación JWT válida
                        .anyRequest().authenticated()
                )

                // Usa nuestro AuthenticationProvider personalizado
                .authenticationProvider(authenticationProvider())

                // Coloca nuestro filtro JWT antes del filtro por username/password
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Define el encoder que se usará para encriptar las contraseñas de los usuarios.
     *
     * BCrypt es una de las opciones más seguras, ya que incluye salt y es adaptable.
     *
     * @return Bean PasswordEncoder configurado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Define el AuthenticationProvider personalizado que usa nuestro UserDetailsService.
     *
     * El DaoAuthenticationProvider compara el usuario y la contraseña usando el
     * UserDetailsService y el PasswordEncoder definidos.
     *
     * @return Bean AuthenticationProvider configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Provee el AuthenticationManager que permite procesar peticiones de autenticación
     * (por ejemplo, durante el login).
     *
     * @param configuration Configuración interna de Spring Security.
     * @return Bean AuthenticationManager configurado.
     * @throws Exception En caso de error al obtener el AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
