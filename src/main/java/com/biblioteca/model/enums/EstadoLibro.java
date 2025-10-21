package com.biblioteca.model.enums;

public enum EstadoLibro {
    DISPONIBLE("Disponible para préstamo"),
    PRESTADO("Prestado a un socio"),
    MANTENIMIENTO("En mantenimiento, no disponible");

    private final String descripcion;

    EstadoLibro(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}