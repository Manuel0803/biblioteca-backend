package com.biblioteca.dto;

import com.biblioteca.model.enums.Rol;
import lombok.Data;

@Data
public class UsuarioResponseDTO {
    private Integer id;
    private String email;
    private Rol rol;
    private String nombre;
    private String apellido;
    private String dni;
    private Boolean activo;
    
    public UsuarioResponseDTO(Integer id, String email, Rol rol, String nombre, String apellido, String dni, Boolean activo) {
        this.id = id;
        this.email = email;
        this.rol = rol;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.activo = activo;
    }
}