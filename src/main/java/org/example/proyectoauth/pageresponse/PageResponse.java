package org.example.proyectoauth.pageresponse;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Clase inmutable que representa una respuesta paginada estándar de una consulta.
 *
 * Se utiliza para encapsular los datos devueltos por una búsqueda con paginación
 * y enviarlos de forma clara y estructurada al cliente.
 *
 * @param <T> Tipo de contenido que contiene la página.
 */
public record PageResponse<T>(
        List<T> content,          // Lista de elementos en la página actual
        int totalPages,           // Total de páginas disponibles
        long totalElements,       // Total de elementos disponibles
        int pageSize,             // Tamaño de página (cantidad de elementos por página)
        int pageNumber,           // Número de la página actual (comienza en 0)
        int totalPageElements,    // Elementos contenidos en la página actual
        boolean empty,            // ¿La página está vacía?
        boolean first,            // ¿Es la primera página?
        boolean last,             // ¿Es la última página?
        String sortBy,            // Campo por el que se ordenó
        String direction          // Dirección de ordenamiento (ASC o DESC)
) {

    /**
     * Crea una instancia de {@link PageResponse} a partir de un objeto {@link Page} de Spring Data.
     *
     * Este método permite transformar directamente una respuesta de la base de datos
     * en un objeto listo para ser enviado al frontend en APIs REST.
     *
     * @param page      Objeto Page devuelto por un repositorio de Spring Data.
     * @param sortBy    Campo por el que se realizó la ordenación.
     * @param direction Dirección de ordenación (ASC o DESC).
     * @return Una instancia de {@link PageResponse} con los datos de la página.
     * @param <T>       Tipo de contenido de la página.
     */
    public static <T> PageResponse<T> of(Page<T> page, String sortBy, String direction) {
        return new PageResponse<>(
                page.getContent(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.getNumber(),
                page.getNumberOfElements(),
                page.isEmpty(),
                page.isFirst(),
                page.isLast(),
                sortBy,
                direction
        );
    }
}
