package com.biblioteca.dto.response;

import com.biblioteca.model.enums.EstadoLibro;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la respuesta de datos del libro")
public class LibroResponse {

    @Schema(description = "ID único del libro", example = "1")
    private Long idLibro;

    @Schema(description = "Título del libro", example = "Cien años de soledad")
    private String titulo;

    @Schema(description = "Autor del libro", example = "Gabriel García Márquez")
    private String autor;

    @Schema(description = "ISBN del libro", example = "978-84-376-0494-7")
    private String isbn;

    @Schema(description = "Estado actual del libro", example = "DISPONIBLE")
    private EstadoLibro estado;

    @Schema(description = "Descripción del estado", example = "Disponible para préstamo")
    private String estadoDescripcion;

    @Schema(description = "Información de la categoría del libro")
    private CategoriaInfo categoria;

    public LibroResponse() {}

    public LibroResponse(Long idLibro, String titulo, String autor, String isbn, 
                        EstadoLibro estado, CategoriaInfo categoria) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.estado = estado;
        this.estadoDescripcion = estado.getDescripcion();
        this.categoria = categoria;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
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

    public EstadoLibro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLibro estado) {
        this.estado = estado;
        this.estadoDescripcion = estado != null ? estado.getDescripcion() : null;
    }

    public String getEstadoDescripcion() {
        return estadoDescripcion;
    }

    public void setEstadoDescripcion(String estadoDescripcion) {
        this.estadoDescripcion = estadoDescripcion;
    }

    public CategoriaInfo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaInfo categoria) {
        this.categoria = categoria;
    }

    public static class CategoriaInfo {
        
        @Schema(description = "ID de la categoría", example = "1")
        private Long idCategoria;
        
        @Schema(description = "Nombre de la categoría", example = "Novela")
        private String nombre;
        
        @Schema(description = "Descripción de la categoría", example = "Obras de ficción narrativa")
        private String descripcion;

        public CategoriaInfo() {}

        public Long getIdCategoria() {
            return idCategoria;
        }

        public void setIdCategoria(Long idCategoria) {
            this.idCategoria = idCategoria;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }
    }
}