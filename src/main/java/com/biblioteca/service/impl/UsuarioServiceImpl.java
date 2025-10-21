package com.biblioteca.service.impl;

import com.biblioteca.model.entity.Usuario;
import com.biblioteca.model.enums.Rol;
import com.biblioteca.repository.UsuarioRepository;
import com.biblioteca.repository.SocioRepository;
import com.biblioteca.service.UsuarioService;
import com.biblioteca.exception.OperationNotAllowedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);
    private final UsuarioRepository usuarioRepository;
    private final SocioRepository socioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, 
                             SocioRepository socioRepository,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.socioRepository = socioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Usuario crearUsuario(Usuario usuario, Rol rol) {
        logger.info("Creando usuario con email: {}", usuario.getEmail());
        
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new OperationNotAllowedException("Ya existe un usuario con este email");
        }
        
        if (usuarioRepository.existsByDni(usuario.getDni())) {
            throw new OperationNotAllowedException("Ya existe un usuario con este DNI");
        }

        Usuario usuarioGuardar = new Usuario();
        usuarioGuardar.setNombre(usuario.getNombre());
        usuarioGuardar.setApellido(usuario.getApellido());
        usuarioGuardar.setEmail(usuario.getEmail());
        usuarioGuardar.setDni(usuario.getDni());
        usuarioGuardar.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuarioGuardar.setRol(rol);
        usuarioGuardar.setActivo(true);

        Usuario usuarioCreado = usuarioRepository.save(usuarioGuardar);
        logger.info("Usuario creado exitosamente con ID: {}", usuarioCreado.getId());
        
        return usuarioCreado;
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuarioPorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuarioPorDni(String dni) {
        return usuarioRepository.existsByDni(dni);
    }

    @Override
    @Transactional
    public void desactivarUsuario(Long id) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
        logger.info("Usuario desactivado con ID: {}", id);
    }

    @Override
    @Transactional
    public void activarUsuario(Long id) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
        logger.info("Usuario activado con ID: {}", id);
    }

    @Override
    @Transactional
    public void cambiarPassword(Long id, String nuevaPassword) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(usuario);
        logger.info("Password cambiado para usuario con ID: {}", id);
    }
}