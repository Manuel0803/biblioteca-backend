package com.biblioteca.dto;

import com.biblioteca.model.enums.Rol;
import lombok.Data;

@Data
public class RegistroUsuarioDTO {
    private String nombre;
    private String apellido;
    private String dni;
    private String email;
    private String password;
    private Rol rol;
}