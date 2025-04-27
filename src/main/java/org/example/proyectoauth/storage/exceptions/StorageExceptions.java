package org.example.proyectoauth.storage.exceptions;

/**
 * Clase base abstracta para todas las excepciones relacionadas con almacenamiento de archivos.
 *
 * Esta clase extiende {@link RuntimeException}, lo que permite lanzar excepciones
 * sin necesidad de declararlas explícitamente en la firma de métodos.
 *
 * Se recomienda extender esta clase para definir tipos de errores más específicos,
 * como errores de carga, eliminación, formato inválido, etc.
 */
public abstract class StorageExceptions extends RuntimeException {

    /**
     * Crea una nueva excepción de almacenamiento con un mensaje descriptivo.
     *
     * @param message Mensaje que explica la causa de la excepción.
     */
    public StorageExceptions(String message) {
        super(message);
    }
}
