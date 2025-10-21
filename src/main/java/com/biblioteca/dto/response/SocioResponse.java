package com.biblioteca.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la respuesta de datos del socio")
public class SocioResponse {

    @Schema(description = "ID único del socio", example = "1")
    private Long idSocio;

    @Schema(description = "Nombre completo del socio", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Número único de socio", example = "1001")
    private Integer nroSocio;

    @Schema(description = "DNI del socio", example = "12345678A")
    private String dni;

    @Schema(description = "Cantidad de préstamos activos", example = "2")
    private Integer prestamosActivos;

    public SocioResponse() {}

    public SocioResponse(Long idSocio, String nombre, Integer nroSocio, String dni, Integer prestamosActivos) {
        this.idSocio = idSocio;
        this.nombre = nombre;
        this.nroSocio = nroSocio;
        this.dni = dni;
        this.prestamosActivos = prestamosActivos;
    }

    public Long getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Long idSocio) {
        this.idSocio = idSocio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNroSocio() {
        return nroSocio;
    }

    public void setNroSocio(Integer nroSocio) {
        this.nroSocio = nroSocio;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Integer getPrestamosActivos() {
        return prestamosActivos;
    }

    public void setPrestamosActivos(Integer prestamosActivos) {
        this.prestamosActivos = prestamosActivos;
    }
}