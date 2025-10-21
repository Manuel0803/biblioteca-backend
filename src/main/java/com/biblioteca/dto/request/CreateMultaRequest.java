package com.biblioteca.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "DTO para la creación manual de una multa")
public class CreateMultaRequest {

    @NotNull(message = "El ID del préstamo es obligatorio")
    @Schema(description = "ID del préstamo asociado a la multa", example = "1", required = true)
    private Long prestamoId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    @Schema(description = "Monto de la multa", example = "50.00", required = true)
    private BigDecimal monto;

    @NotNull(message = "El motivo es obligatorio")
    @Schema(description = "Motivo de la multa", example = "Daño grave en las páginas del libro", required = true)
    private String motivo;

    @Schema(description = "Descripción detallada de la multa", example = "Libro con páginas 15-20 rotas y cubierta despegada")
    private String descripcion;

    public CreateMultaRequest() {}

    public CreateMultaRequest(Long prestamoId, BigDecimal monto, String motivo, String descripcion) {
        this.prestamoId = prestamoId;
        this.monto = monto;
        this.motivo = motivo;
        this.descripcion = descripcion;
    }

    public Long getPrestamoId() {
        return prestamoId;
    }

    public void setPrestamoId(Long prestamoId) {
        this.prestamoId = prestamoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}