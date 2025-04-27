package org.example.proyectoauth.config.storage;

import lombok.extern.slf4j.Slf4j;
import org.example.proyectoauth.storage.services.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración inicial del servicio de almacenamiento de archivos.
 *
 * Esta clase se encarga de preparar el entorno de almacenamiento al iniciar
 * la aplicación, y puede opcionalmente eliminar archivos previos si así se indica.
 */
@Configuration
@Slf4j
public class StorageConfig {

    /**
     * Bean que se ejecuta automáticamente al iniciar la aplicación.
     * Se utiliza para inicializar el sistema de almacenamiento, y
     * opcionalmente eliminar todos los archivos existentes según la configuración.
     *
     * @param storageService Servicio de almacenamiento inyectado.
     * @param deleteAll      Variable configurada en application.properties para indicar si se deben borrar los archivos.
     * @return Un {@link CommandLineRunner} que ejecuta la lógica de inicio.
     */
    @Bean
    public CommandLineRunner init(StorageService storageService, @Value("${upload.delete}") String deleteAll) {
        return args -> {
            // Si la propiedad está activada, borra todos los archivos existentes al iniciar
            if ("true".equalsIgnoreCase(deleteAll)) {
                log.info("[Storage] → Borrando todos los archivos del almacenamiento...");
                storageService.deleteAll();
            }

            // Inicializa el sistema de almacenamiento (por ejemplo, creando carpetas)
            log.info("[Storage] → Inicializando sistema de almacenamiento...");
            storageService.init();
        };
    }
}
