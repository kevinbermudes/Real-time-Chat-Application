package org.example.proyectoauth.storage.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada que representa un error de solicitud incorrecta
 * en operaciones relacionadas con el almacenamiento de archivos.
 *
 * Se lanza comúnmente cuando se intenta realizar una operación inválida
 * como subir un archivo vacío o solicitar un archivo inexistente.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StorageBadRequestException extends StorageExceptions {

    /**
     * Crea una nueva instancia de la excepción con un mensaje personalizado.
     *
     * @param message Mensaje que describe la causa del error.
     */
    public StorageBadRequestException(String message) {
        super(message);
    }
}
