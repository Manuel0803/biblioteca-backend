package com.biblioteca.model.enums;

public enum EstadoDevolucion {
    BUEN_ESTADO("En buen estado", false),
    DANIO_LEVE("Daño leve (subrayado, rayones)", true),
    DANIO_GRAVE("Daño grave (páginas rotas, portada dañada)", true),
    PERDIDA("Pérdida total", true);

    private final String descripcion;
    private final boolean aplicaMulta;

    EstadoDevolucion(String descripcion, boolean aplicaMulta) {
        this.descripcion = descripcion;
        this.aplicaMulta = aplicaMulta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isAplicaMulta() {
        return aplicaMulta;
    }
}