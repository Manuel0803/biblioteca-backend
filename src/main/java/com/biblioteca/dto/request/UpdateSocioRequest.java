package com.biblioteca.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la actualización de un socio existente")
public class UpdateSocioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    @Schema(description = "Nombre completo del socio", example = "Juan Pérez", required = true)
    private String nombre;

    @NotNull(message = "El número de socio es obligatorio")
    @Schema(description = "Número único de socio", example = "1001", required = true)
    private Integer nroSocio;

    public UpdateSocioRequest() {}

    public UpdateSocioRequest(String nombre, Integer nroSocio) {
        this.nombre = nombre;
        this.nroSocio = nroSocio;
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
}