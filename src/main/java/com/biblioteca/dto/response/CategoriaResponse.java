package com.biblioteca.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la respuesta de datos de la categoría")
public class CategoriaResponse {

    @Schema(description = "ID único de la categoría", example = "1")
    private Long idCategoria;

    @Schema(description = "Nombre de la categoría", example = "Novela")
    private String nombre;

    @Schema(description = "Descripción de la categoría", example = "Obras de ficción narrativa")
    private String descripcion;

    @Schema(description = "Cantidad de libros en esta categoría", example = "15")
    private Integer cantidadLibros;

    public CategoriaResponse() {}

    public CategoriaResponse(Long idCategoria, String nombre, String descripcion, Integer cantidadLibros) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidadLibros = cantidadLibros;
    }

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

    public Integer getCantidadLibros() {
        return cantidadLibros;
    }

    public void setCantidadLibros(Integer cantidadLibros) {
        this.cantidadLibros = cantidadLibros;
    }
}