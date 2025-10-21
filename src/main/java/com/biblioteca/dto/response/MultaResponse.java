package com.biblioteca.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "DTO para la respuesta de datos de la multa")
public class MultaResponse {

    @Schema(description = "ID único de la multa", example = "1")
    private Long idMulta;

    @Schema(description = "Monto de la multa", example = "50.00")
    private BigDecimal monto;

    @Schema(description = "Motivo de la multa", example = "Retraso en la devolución: 5 días de retraso")
    private String motivo;

    @Schema(description = "Indica si la multa está pagada", example = "false")
    private Boolean pagada;

    @Schema(description = "Indica si la multa está activa (no pagada)", example = "true")
    private Boolean activa;

    @Schema(description = "Información del préstamo asociado")
    private PrestamoResponse prestamo;

    public MultaResponse() {}

    public Long getIdMulta() {
        return idMulta;
    }

    public void setIdMulta(Long idMulta) {
        this.idMulta = idMulta;
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

    public Boolean getPagada() {
        return pagada;
    }

    public void setPagada(Boolean pagada) {
        this.pagada = pagada;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public PrestamoResponse getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(PrestamoResponse prestamo) {
        this.prestamo = prestamo;
    }
}