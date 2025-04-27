package org.example.proyectoauth.config.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS (Cross-Origin Resource Sharing).
 *
 * Permite controlar desde qué dominios externos se pueden hacer solicitudes a esta API.
 * Los orígenes permitidos se cargan dinámicamente desde el archivo de propiedades
 * según el perfil activo (por ejemplo: dev o prod).
 */
@Configuration
public class CorsConfig {

    /**
     * Lista de orígenes permitidos para solicitudes CORS, separados por comas.
     * Se obtiene desde application.properties o desde el archivo de perfil activo.
     */
    @Value("${cors.allowed-origins}")
    private String[] allowedOrigins;

    /**
     * Define la configuración global de CORS que se aplicará a todos los endpoints.
     *
     * @return Bean WebMvcConfigurer con la configuración personalizada de CORS.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * Aplica las reglas CORS:
             * - Acepta peticiones desde los orígenes definidos en allowedOrigins.
             * - Permite todos los métodos HTTP y encabezados.
             * - Cachea la política de CORS por 1 hora (3600 segundos).
             */
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("*")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
}
