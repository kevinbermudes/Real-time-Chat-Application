package org.example.proyectoauth.storage.services;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Interfaz que define las operaciones básicas de un sistema de almacenamiento de archivos.
 *
 * Esta abstracción permite implementar diferentes soluciones de almacenamiento
 * como almacenamiento en disco, en la nube, en base de datos, etc.
 */
public interface StorageService {

    /**
     * Inicializa el almacenamiento creando la estructura base si es necesario.
     */
    void init();

    /**
     * Almacena un archivo.
     *
     * @param file Archivo a almacenar.
     * @return Nombre generado del archivo almacenado.
     */
    String store(MultipartFile file);

    /**
     * Carga todos los archivos disponibles.
     *
     * @return Stream con las rutas relativas de los archivos almacenados.
     */
    Stream<Path> loadAll();

    /**
     * Carga un archivo por su nombre como {@link Path}.
     *
     * @param filename Nombre del archivo.
     * @return Ruta al archivo.
     */
    Path load(String filename);

    /**
     * Carga un archivo como {@link Resource} accesible para controladores web.
     *
     * @param filename Nombre del archivo.
     * @return Recurso del archivo cargado.
     */
    Resource loadAsResource(String filename);

    /**
     * Elimina un archivo específico.
     *
     * @param filename Nombre del archivo a eliminar.
     */
    void delete(String filename);

    /**
     * Elimina todos los archivos almacenados.
     */
    void deleteAll();

    /**
     * Genera la URL pública de un archivo almacenado.
     *
     * @param filename Nombre del archivo.
     * @return URL del archivo.
     */
    String getUrl(String filename);
}
