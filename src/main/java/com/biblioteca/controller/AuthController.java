package com.biblioteca.controller;

import com.biblioteca.dto.LoginDTO;
import com.biblioteca.dto.RegistroUsuarioDTO;
import com.biblioteca.dto.UsuarioResponseDTO;
import com.biblioteca.security.JwtUtil;
import com.biblioteca.security.UserDetailsServiceImpl;
import com.biblioteca.service.AuthService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "https://biblioteca-frontend-host.vercel.app") 
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetailsServiceImpl.UserDetailsImpl userDetails = (UserDetailsServiceImpl.UserDetailsImpl) authentication.getPrincipal();

            Integer userId = userDetails.getId();
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                    .orElse("UNKNOWN");

            String jwt = jwtUtil.generateJwtToken(
                userDetails.getUsername(), 
                role, 
                userId, 
                userDetails.getDni(),
                userDetails.getApellido(),
                userDetails.getNombre()
            );

            LoginResponse response = new LoginResponse(
                jwt,
                "Bearer",
                userId,
                userDetails.getUsername(),
                role,
                userDetails.getNombre(),
                userDetails.getApellido(),
                userDetails.getDni()
            );

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody RegistroUsuarioDTO usuarioDTO) {
        try {
            if (authService.buscarPorEmail(usuarioDTO.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("El email ya está registrado");
            }

            UsuarioResponseDTO nuevoUsuario = authService.registrarUsuario(usuarioDTO);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Servidor de autenticación funcionando");
    }

    @Data
    public static class LoginResponse {
        private String token;
        private String type = "Bearer";
        private Integer id;
        private String email;
        private String rol;
        private String nombre;
        private String apellido;
        private String dni;

        public LoginResponse(String token, String type, Integer id, String email, String rol, 
                             String nombre, String apellido, String dni) {
            this.token = token;
            this.type = type;
            this.id = id;
            this.email = email;
            this.rol = rol;
            this.nombre = nombre;
            this.apellido = apellido;
            this.dni = dni;
        }
    }
}