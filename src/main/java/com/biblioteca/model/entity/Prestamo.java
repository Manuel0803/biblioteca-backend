package com.biblioteca.model.entity;

import com.biblioteca.model.enums.EstadoPrestamo;
import com.biblioteca.model.enums.EstadoDevolucion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "prestamo")
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_prestamo")
    private Long idPrestamo;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_prevista")
    private LocalDate fechaFinPrevista;

    @Column(name = "fecha_devolucion_real")
    private LocalDate fechaDevolucionReal;

    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPrestamo estado;

    @NotNull(message = "El libro es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_libro", nullable = false)
    private Libro libro;

    @NotNull(message = "El socio es obligatorio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio socio;

    @OneToOne(mappedBy = "prestamo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Multa multa;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_devolucion", length = 20)
    private EstadoDevolucion estadoDevolucion;

    @Column(name = "observaciones_devolucion", length = 500)
    private String observacionesDevolucion;

    @Column(name = "tiene_danio")
    private Boolean tieneDanio;

    public Prestamo() {
        this.fechaInicio = LocalDate.now();
        this.estado = EstadoPrestamo.ACTIVO;
    }

    public Prestamo(Libro libro, Socio socio) {
        this();
        this.libro = libro;
        this.socio = socio;
    }

    public Prestamo(Libro libro, Socio socio, LocalDate fechaInicio) {
        this();
        this.libro = libro;
        this.socio = socio;
        this.fechaInicio = fechaInicio;
    }

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

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Multa getMulta() {
        return multa;
    }

    public void setMulta(Multa multa) {
        this.multa = multa;
    }

    public EstadoDevolucion getEstadoDevolucion() {
        return estadoDevolucion;
    }

    public void setEstadoDevolucion(EstadoDevolucion estadoDevolucion) {
        this.estadoDevolucion = estadoDevolucion;
    }

    public String getObservacionesDevolucion() {
        return observacionesDevolucion;
    }

    public void setObservacionesDevolucion(String observacionesDevolucion) {
        this.observacionesDevolucion = observacionesDevolucion;
    }

    public Boolean getTieneDanio() {
        return tieneDanio;
    }

    public void setTieneDanio(Boolean tieneDanio) {
        this.tieneDanio = tieneDanio;
    }

    public boolean estaActivo() {
        return EstadoPrestamo.ACTIVO.equals(this.estado);
    }

    public boolean estaVencido() {
        return EstadoPrestamo.VENCIDO.equals(this.estado);
    }

    public boolean estaFinalizado() {
        return EstadoPrestamo.FINALIZADO.equals(this.estado);
    }

    public void finalizarPrestamo() {
        if (this.estado == EstadoPrestamo.FINALIZADO) {
            throw new IllegalStateException("El préstamo ya está finalizado");
        }
        
        this.estado = EstadoPrestamo.FINALIZADO;
        this.fechaDevolucionReal = LocalDate.now();
        
        if (this.libro != null) {
            this.libro.marcarComoDisponible();
        }
    }

    public void marcarComoVencido() {
        if (this.estado == EstadoPrestamo.ACTIVO && 
            this.fechaFinPrevista != null && 
            LocalDate.now().isAfter(this.fechaFinPrevista)) {
            this.estado = EstadoPrestamo.VENCIDO;
        }
    }

    public int calcularDiasRetraso() {
        if (estaFinalizado() && this.fechaDevolucionReal != null) {
            LocalDate fechaLimite = this.fechaInicio.plusDays(15);
            if (this.fechaDevolucionReal.isAfter(fechaLimite)) {
                return (int) java.time.temporal.ChronoUnit.DAYS.between(fechaLimite, this.fechaDevolucionReal);
            }
        } else if (estaActivo() || estaVencido()) {
            LocalDate fechaLimite = this.fechaInicio.plusDays(15);
            if (LocalDate.now().isAfter(fechaLimite)) {
                return (int) java.time.temporal.ChronoUnit.DAYS.between(fechaLimite, LocalDate.now());
            }
        }
        return 0;
    }

    public boolean tieneMulta() {
        return this.multa != null;
    }
}