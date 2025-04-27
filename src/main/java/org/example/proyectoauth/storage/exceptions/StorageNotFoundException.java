package org.example.proyectoauth.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada que representa un error de recurso no encontrado
 * durante operaciones de almacenamiento de archivos.
 *
 * Se lanza, por ejemplo, cuando se intenta acceder a un archivo inexistente
 * o que fue eliminado del sistema.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class StorageNotFoundException extends StorageExceptions {

    /**
     * Crea una nueva excepción indicando que el recurso solicitado no existe.
     *
     * @param message Mensaje que describe la causa del error.
     */
    public StorageNotFoundException(String message) {
        super(message);
    }
}
