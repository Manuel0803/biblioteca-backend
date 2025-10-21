package com.biblioteca.dto.request;

import com.biblioteca.model.enums.EstadoDevolucion;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para la devolución de un préstamo")
public class DevolucionPrestamoRequest {

    @NotNull(message = "El estado de devolución es obligatorio")
    @Schema(description = "Estado en que se devuelve el libro", required = true)
    private EstadoDevolucion estadoDevolucion;

    @Schema(description = "Observaciones sobre el estado del libro", example = "Libro con algunas páginas subrayadas")
    private String observaciones;

    public DevolucionPrestamoRequest() {}

    public DevolucionPrestamoRequest(EstadoDevolucion estadoDevolucion, String observaciones) {
        this.estadoDevolucion = estadoDevolucion;
        this.observaciones = observaciones;
    }

    public EstadoDevolucion getEstadoDevolucion() {
        return estadoDevolucion;
    }

    public void setEstadoDevolucion(EstadoDevolucion estadoDevolucion) {
        this.estadoDevolucion = estadoDevolucion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}