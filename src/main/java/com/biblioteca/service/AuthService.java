package com.biblioteca.service;

import com.biblioteca.dto.RegistroUsuarioDTO;
import com.biblioteca.dto.UsuarioResponseDTO;
import com.biblioteca.exception.ResourceAlreadyExistsException;
import com.biblioteca.model.entity.Usuario;
import com.biblioteca.model.enums.Rol;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public UsuarioResponseDTO registrarUsuario(RegistroUsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException("El email ya está registrado: " + dto.getEmail());
        }

        if (usuarioRepository.existsByDni(dto.getDni())) {
            throw new ResourceAlreadyExistsException("El DNI ya está registrado: " + dto.getDni());
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setDni(dto.getDni());
        usuario.setRol(dto.getRol() != null ? dto.getRol() : Rol.SOCIO);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        
        return new UsuarioResponseDTO(
            usuarioGuardado.getId(),
            usuarioGuardado.getEmail(),
            usuarioGuardado.getRol(),
            usuarioGuardado.getNombre(),
            usuarioGuardado.getApellido(),
            usuarioGuardado.getDni(),
            usuarioGuardado.getActivo()
        );
    }
}