package org.example.proyectoauth.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger/OpenAPI para la documentación de la API.
 *
 * Incluye definición del esquema de seguridad (Bearer JWT), información básica
 * de la API y agrupación de endpoints para facilitar su mantenimiento y visualización.
 */
@Configuration
public class SwaggerConfig {

    @Value("${api.version}")
    private String apiVersion;

    /**
     * Define el esquema de seguridad basado en tokens JWT para proteger los endpoints.
     *
     * @return El esquema de seguridad con formato Bearer.
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    /**
     * Configura la instancia principal de OpenAPI con:
     * - Título
     * - Descripción
     * - Contacto
     * - Licencia
     * - Documentación externa
     * - Seguridad con JWT
     *
     * ¡Recuerda personalizar estos datos en tu proyecto final!
     *
     * @return Configuración OpenAPI lista para usarse en Swagger UI.
     */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Introducir título del proyecto")
                                .version("1.0.0")
                                .description("Descripción breve del propósito de la API")
                                .termsOfService("https://tusitio.com/terminos") // o enlace a tu repositorio
                                .license(
                                        new License()
                                                .name("Nombre de la licencia")
                                                .url("https://enlace-a-la-licencia.com")
                                )
                                .contact(
                                        new Contact()
                                                .name("Nombre del equipo o desarrollador")
                                                .email("tucorreo@dominio.com")
                                                .url("https://tu-sitio-o-repo.com")
                                )
                )
                .externalDocs(
                        new ExternalDocumentation()
                                .description("Documentación externa del proyecto")
                                .url("https://enlace-a-documentacion.com")
                )
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()));
    }

    /**
     * Agrupa los endpoints de la API según su ruta base.
     * Esta agrupación se muestra en Swagger UI bajo el nombre definido.
     *
     * Puedes modificar las rutas incluidas y el nombre del grupo según tu necesidad.
     *
     * @return Agrupación de endpoints para Swagger/OpenAPI.
     */
    @Bean
    public GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("API Pública") // Nombre visible en Swagger UI
                .pathsToMatch(
                        "/" + apiVersion + "/auth/**",
                        "/" + apiVersion + "/users/**"
                )
                .displayName("Introducir nombre para mostrar en Swagger UI")
                .build();
    }
}
