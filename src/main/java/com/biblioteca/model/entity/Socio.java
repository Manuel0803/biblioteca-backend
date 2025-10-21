package com.biblioteca.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "socio")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")
    private Long idSocio;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @NotNull(message = "El número de socio es obligatorio")
    @Column(name = "nro_socio", nullable = false, unique = true)
    private Integer nroSocio;

    @NotBlank(message = "El DNI es obligatorio")
    @Column(name = "dni", nullable = false, unique = true, length = 15)
    private String dni;

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prestamo> prestamos = new ArrayList<>();

    public Socio() {}

    public Socio(String nombre, Integer nroSocio, String dni) {
        this.nombre = nombre;
        this.nroSocio = nroSocio;
        this.dni = dni;
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

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public void setPrestamos(List<Prestamo> prestamos) {
        this.prestamos = prestamos;
    }

    public void agregarPrestamo(Prestamo prestamo) {
        prestamos.add(prestamo);
        prestamo.setSocio(this);
    }

    public void removerPrestamo(Prestamo prestamo) {
        prestamos.remove(prestamo);
        prestamo.setSocio(null);
    }

    public boolean tienePrestamosActivos() {
        return prestamos.stream().anyMatch(Prestamo::estaActivo);
    }
}