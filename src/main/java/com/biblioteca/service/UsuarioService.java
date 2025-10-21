package com.biblioteca.service;

import com.biblioteca.model.entity.Usuario;
import com.biblioteca.model.enums.Rol;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario crearUsuario(Usuario usuario, Rol rol);
    Usuario obtenerUsuarioPorId(Long id);
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
    List<Usuario> obtenerTodosLosUsuarios();
    List<Usuario> obtenerUsuariosPorRol(Rol rol);
    boolean existeUsuarioPorEmail(String email);
    boolean existeUsuarioPorDni(String dni);
    void desactivarUsuario(Long id);
    void activarUsuario(Long id);
    void cambiarPassword(Long id, String nuevaPassword);
}