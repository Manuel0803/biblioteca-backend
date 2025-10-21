package com.biblioteca.dto.response;

import com.biblioteca.model.enums.EstadoPrestamo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "DTO para la respuesta de datos del préstamo")
public class PrestamoResponse {

    @Schema(description = "ID único del préstamo", example = "1")
    private Long idPrestamo;

    @Schema(description = "Fecha de inicio del préstamo", example = "2024-01-15")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin prevista del préstamo", example = "2024-01-30")
    private LocalDate fechaFinPrevista;

    @Schema(description = "Fecha real de devolución", example = "2024-01-28")
    private LocalDate fechaDevolucionReal;

    @Schema(description = "Estado del préstamo", example = "ACTIVO")
    private EstadoPrestamo estado;

    @Schema(description = "Información del libro")
    private LibroResponse libro;

    @Schema(description = "Información del socio")
    private SocioResponse socio;

    @Schema(description = "Información básica del socio (para uso en multas)")
    private SocioInfo socioInfo;

    @Schema(description = "Indica si el préstamo está activo", example = "true")
    private Boolean activo;

    @Schema(description = "Días de retraso si aplica", example = "2")
    private Integer diasRetraso;

    @Schema(description = "Indica si tiene multa asociada", example = "false")
    private Boolean tieneMulta;

    public PrestamoResponse() {}

    public Long getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(Long idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinPrevista() {
        return fechaFinPrevista;
    }

    public void setFechaFinPrevista(LocalDate fechaFinPrevista) {
        this.fechaFinPrevista = fechaFinPrevista;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    public void setEstado(EstadoPrestamo estado) {
        this.estado = estado;
    }

    public LibroResponse getLibro() {
        return libro;
    }

    public void setLibro(LibroResponse libro) {
        this.libro = libro;
    }

    public SocioResponse getSocio() {
        return socio;
    }

    public void setSocio(SocioResponse socio) {
        this.socio = socio;
    }

    public SocioInfo getSocioInfo() {
        return socioInfo;
    }

    public void setSocioInfo(SocioInfo socioInfo) {
        this.socioInfo = socioInfo;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Integer getDiasRetraso() {
        return diasRetraso;
    }

    public void setDiasRetraso(Integer diasRetraso) {
        this.diasRetraso = diasRetraso;
    }

    public Boolean getTieneMulta() {
        return tieneMulta;
    }

    public void setTieneMulta(Boolean tieneMulta) {
        this.tieneMulta = tieneMulta;
    }

    public static class SocioInfo {
        
        @Schema(description = "ID del socio", example = "1")
        private Long idSocio;
        
        @Schema(description = "Nombre del socio", example = "Juan Pérez")
        private String nombre;
        
        @Schema(description = "Número de socio", example = "1001")
        private Integer nroSocio;

        public SocioInfo() {}

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
    }
}