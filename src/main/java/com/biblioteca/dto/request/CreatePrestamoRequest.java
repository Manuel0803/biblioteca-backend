package com.biblioteca.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Schema(description = "DTO para la creación de un nuevo préstamo")
public class CreatePrestamoRequest {

    @NotNull(message = "El ID del libro es obligatorio")
    @Schema(description = "ID del libro a prestar", example = "1", required = true)
    private Long idLibro;

    @NotNull(message = "El ID del socio es obligatorio")
    @Schema(description = "ID del socio que realiza el préstamo", example = "1", required = true)
    private Long idSocio;

    @Schema(description = "Fecha de inicio del préstamo (opcional, por defecto fecha actual)", example = "2024-01-15")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin prevista es obligatoria")
    @Schema(description = "Fecha de fin prevista del préstamo (fecha de vencimiento)", example = "2024-01-30", required = true)
    private LocalDate fechaFin;

    public CreatePrestamoRequest() {}

    public CreatePrestamoRequest(Long idLibro, Long idSocio, LocalDate fechaFin) {
        this.idLibro = idLibro;
        this.idSocio = idSocio;
        this.fechaInicio = LocalDate.now();
        this.fechaFin = fechaFin;
    }

    public Long getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Long idLibro) {
        this.idLibro = idLibro;
    }

    public Long getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Long idSocio) {
        this.idSocio = idSocio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}