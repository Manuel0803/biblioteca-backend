package com.biblioteca.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la creación de un nuevo libro")
public class CreateLibroRequest {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no puede exceder los 255 caracteres")
    @Schema(description = "Título del libro", example = "Cien años de soledad", required = true)
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 255, message = "El autor no puede exceder los 255 caracteres")
    @Schema(description = "Autor del libro", example = "Gabriel García Márquez", required = true)
    private String autor;

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(max = 20, message = "El ISBN no puede exceder los 20 caracteres")
    @Schema(description = "ISBN del libro", example = "978-84-376-0494-7", required = true)
    private String isbn;

    @NotNull(message = "La categoría es obligatoria")
    @Schema(description = "ID de la categoría del libro", example = "1", required = true)
    private Long idCategoria;

    public CreateLibroRequest() {}

    public CreateLibroRequest(String titulo, String autor, String isbn, Long idCategoria) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.idCategoria = idCategoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }
}