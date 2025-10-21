package com.biblioteca.repository;

import com.biblioteca.model.entity.Usuario;
import com.biblioteca.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol(Rol rol);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
}