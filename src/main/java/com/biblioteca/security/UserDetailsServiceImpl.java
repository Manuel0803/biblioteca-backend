package com.biblioteca.security;

import com.biblioteca.model.entity.Usuario;
import com.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return new UserDetailsImpl(usuario);
    }

    public static class UserDetailsImpl implements UserDetails {
        private final Usuario usuario;

        public UserDetailsImpl(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()));
        }

        @Override
        public String getPassword() {
            return usuario.getPassword();
        }

        @Override
        public String getUsername() {
            return usuario.getEmail();
        }

        public Integer getId() {
            return usuario.getId();
        }

        public String getNombre() {
            return usuario.getNombre();
        }

        public String getApellido() {
            return usuario.getApellido();
        }

        public String getDni() {
            return usuario.getDni();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return usuario.getActivo();
        }
    }
}