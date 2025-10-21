package com.biblioteca.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para la creación de un nuevo socio")
public class CreateSocioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder los 255 caracteres")
    @Schema(description = "Nombre completo del socio", example = "Juan Pérez", required = true)
    private String nombre;

    @NotNull(message = "El número de socio es obligatorio")
    @Schema(description = "Número único de socio", example = "1001", required = true)
    private Integer nroSocio;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 15, message = "El DNI no puede exceder los 15 caracteres")
    @Schema(description = "DNI del socio", example = "12345678A", required = true)
    private String dni;

    public CreateSocioRequest() {}

    public CreateSocioRequest(String nombre, Integer nroSocio, String dni) {
        this.nombre = nombre;
        this.nroSocio = nroSocio;
        this.dni = dni;
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
}