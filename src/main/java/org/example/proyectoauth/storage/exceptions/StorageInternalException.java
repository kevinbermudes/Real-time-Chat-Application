package org.example.proyectoauth.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada que representa un error interno del servidor
 * durante operaciones relacionadas con el almacenamiento de archivos.
 *
 * Esta excepción se lanza cuando ocurre una condición inesperada o no controlada,
 * como fallos al leer, escribir o eliminar archivos.
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class StorageInternalException extends StorageExceptions {

    /**
     * Crea una nueva excepción de error interno con un mensaje descriptivo.
     *
     * @param message Mensaje que explica la causa del error interno.
     */
    public StorageInternalException(String message) {
        super(message);
    }
}
