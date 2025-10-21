package com.biblioteca.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "multa")
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_multa")
    private Long idMulta;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser positivo")
    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @NotBlank(message = "El motivo es obligatorio")
    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @NotNull(message = "El préstamo es obligatorio")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prestamo", nullable = false, unique = true)
    private Prestamo prestamo;

    @Column(name = "pagada", nullable = false)
    private Boolean pagada = false;

    public Multa() {}

    public Multa(BigDecimal monto, String motivo, Prestamo prestamo) {
        this.monto = monto;
        this.motivo = motivo;
        this.prestamo = prestamo;
    }

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

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public Boolean getPagada() {
        return pagada;
    }

    public void setPagada(Boolean pagada) {
        this.pagada = pagada;
    }

    public void marcarComoPagada() {
        this.pagada = true;
    }

    public boolean estaActiva() {
        return !this.pagada;
    }
}