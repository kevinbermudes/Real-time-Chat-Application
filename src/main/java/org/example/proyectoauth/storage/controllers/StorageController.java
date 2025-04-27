package org.example.proyectoauth.storage.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.proyectoauth.storage.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * Controlador REST para la gestión de almacenamiento de archivos.
 *
 * Permite servir archivos desde el sistema de almacenamiento al cliente.
 */
@RestController
@Slf4j
@RequestMapping("/storage")
public class StorageController {

    private final StorageService storageService;

    /**
     * Constructor del controlador.
     *
     * @param storageService Servicio de almacenamiento a inyectar.
     */
    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Endpoint para obtener un archivo desde el almacenamiento.
     *
     * @param filename Nombre del archivo a recuperar.
     * @param request  Objeto HTTP para determinar el tipo MIME del archivo.
     * @return Archivo como recurso, con tipo de contenido adecuado.
     */
    @GetMapping(value = "{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, HttpServletRequest request) {
        log.info("[STORAGE] Solicitando archivo: {}", filename);

        Resource file = storageService.loadAsResource(filename);

        String contentType;
        try {
            contentType = request.getServletContext().getMimeType(file.getFile().getAbsolutePath());
        } catch (IOException ex) {
            log.error("[STORAGE] Error al determinar el tipo MIME del archivo: {}", filename, ex);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede determinar el tipo de archivo");
        }

        if (contentType == null) {
            log.warn("[STORAGE] Tipo MIME no determinado, usando 'application/octet-stream'");
            contentType = "application/octet-stream";
        }

        log.info("[STORAGE] Archivo '{}' preparado para ser enviado (MIME: {})", filename, contentType);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }

    // Puedes habilitar este método cuando implementes la subida:
    /*
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestPart("file") MultipartFile file) {

        if (file.isEmpty()) {
            log.warn("[STORAGE] Intento de subida de archivo vacío");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede subir un fichero vacío");
        }

        String filename = storageService.store(file);
        String fileUrl = storageService.getUrl(filename);
        log.info("[STORAGE] Archivo '{}' subido exitosamente. URL: {}", filename, fileUrl);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("url", fileUrl));
    }
    */

    // Aquí podrías añadir más endpoints en el futuro:
    // - DELETE /storage/{filename}
    // - GET /storage/list
}
