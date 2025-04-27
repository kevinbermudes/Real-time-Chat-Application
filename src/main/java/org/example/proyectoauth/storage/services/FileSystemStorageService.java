package org.example.proyectoauth.storage.services;

import lombok.extern.slf4j.Slf4j;
import org.example.proyectoauth.storage.controllers.StorageController;
import org.example.proyectoauth.storage.exceptions.StorageBadRequestException;
import org.example.proyectoauth.storage.exceptions.StorageInternalException;
import org.example.proyectoauth.storage.exceptions.StorageNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * Servicio de almacenamiento basado en sistema de archivos local.
 *
 * Se encarga de almacenar, recuperar y eliminar archivos desde una ubicación
 * definida en el sistema de archivos del servidor.
 */
@Service
@Slf4j
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final List<String> allowedExtensions = List.of("png", "jpg", "jpeg", "gif");

    /**
     * Constructor que recibe la ubicación raíz del almacenamiento desde propiedades.
     *
     * @param rootLocation Ubicación base definida en application.properties.
     */
    public FileSystemStorageService(@Value("${upload.root-location}") String rootLocation) {
        this.rootLocation = Paths.get(rootLocation);
    }

    /**
     * Inicializa el directorio de almacenamiento creando la ruta si no existe.
     */
    @Override
    public void init() {
        log.info("[STORAGE] Inicializando almacenamiento en: {}", rootLocation);
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageInternalException("No se puede inicializar el almacenamiento: " + e.getMessage());
        }
    }

    /**
     * Almacena un archivo en el sistema de archivos.
     *
     * @param file Archivo a almacenar.
     * @return Nombre único generado para el archivo.
     */
    @Override
    public String store(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        if (file.isEmpty()) {
            log.warn("[STORAGE] Archivo vacío: {}", originalFilename);
            throw new StorageBadRequestException("Fichero vacío: " + originalFilename);
        }

        if (originalFilename.contains("..")) {
            throw new StorageBadRequestException("Nombre de archivo no válido: " + originalFilename);
        }

        String extension = StringUtils.getFilenameExtension(originalFilename);
        if (extension == null || allowedExtensions.stream().noneMatch(ex -> ex.equalsIgnoreCase(extension))) {
            throw new StorageBadRequestException("Extensión no permitida: ." + extension);
        }

        String baseName = originalFilename.replace("." + extension, "");
        String storedFilename = System.currentTimeMillis() + "_" + baseName + "." + extension;

        try (InputStream inputStream = file.getInputStream()) {
            log.info("[STORAGE] Almacenando archivo como: {}", storedFilename);
            Files.copy(inputStream, this.rootLocation.resolve(storedFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            return storedFilename;
        } catch (IOException e) {
            throw new StorageInternalException("Error al almacenar archivo: " + originalFilename + ". " + e.getMessage());
        }
    }

    /**
     * Devuelve una secuencia de rutas relativas a todos los archivos almacenados.
     */
    @Override
    public Stream<Path> loadAll() {
        log.info("[STORAGE] Cargando lista de archivos");
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageInternalException("Error al leer archivos almacenados: " + e.getMessage());
        }
    }

    /**
     * Carga un archivo por su nombre como {@link Path}.
     */
    @Override
    public Path load(String filename) {
        log.info("[STORAGE] Cargando ruta del archivo: {}", filename);
        return rootLocation.resolve(filename);
    }

    /**
     * Carga un archivo como recurso accesible por Spring.
     */
    @Override
    public Resource loadAsResource(String filename) {
        log.info("[STORAGE] Cargando recurso para archivo: {}", filename);
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new StorageNotFoundException("Archivo no accesible o no encontrado: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageNotFoundException("Error al leer archivo: " + filename + ". " + e.getMessage());
        }
    }

    /**
     * Elimina un archivo específico del sistema de archivos.
     */
    @Override
    public void delete(String filename) {
        String cleanFilename = StringUtils.getFilename(filename);
        try {
            log.info("[STORAGE] Eliminando archivo: {}", cleanFilename);
            Files.deleteIfExists(load(cleanFilename));
        } catch (IOException e) {
            throw new StorageInternalException("No se pudo eliminar el archivo: " + filename + ". " + e.getMessage());
        }
    }

    /**
     * Elimina todos los archivos del directorio de almacenamiento.
     */
    @Override
    public void deleteAll() {
        log.info("[STORAGE] Eliminando todos los archivos del almacenamiento");
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Genera la URL pública de acceso a un archivo.
     */
    @Override
    public String getUrl(String filename) {
        log.info("[STORAGE] Generando URL pública para: {}", filename);
        return MvcUriComponentsBuilder
                .fromMethodName(StorageController.class, "serveFile", filename, null)
                .build()
                .toUriString();
    }
}
