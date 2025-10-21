package com.biblioteca.model.enums;

public enum Rol {
    SOCIO("Socio", "Usuario regular que puede realizar préstamos"),
    BIBLIOTECARIO("Bibliotecario", "Puede gestionar préstamos y multas"),  
    ADMIN("Administrador", "Acceso completo al sistema");

    private final String nombre;
    private final String descripcion;

    Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static Rol fromString(String text) {
        for (Rol rol : Rol.values()) {
            if (rol.name().equalsIgnoreCase(text)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("Rol no válido: " + text);
    }
}