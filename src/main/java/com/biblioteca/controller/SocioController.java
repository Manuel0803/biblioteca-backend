package com.biblioteca.controller;

import com.biblioteca.dto.request.CreateSocioRequest;
import com.biblioteca.dto.request.UpdateSocioRequest;
import com.biblioteca.dto.response.SocioResponse;
import com.biblioteca.service.SocioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
@Tag(name = "Socios", description = "API para la gestión de socios")
@CrossOrigin(origins = "https://biblioteca-frontend-host.vercel.app") 
public class SocioController {

    private final SocioService socioService;

    public SocioController(SocioService socioService) {
        this.socioService = socioService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo socio", description = "Registra un nuevo socio en el sistema")
    public ResponseEntity<SocioResponse> crearSocio(@Valid @RequestBody CreateSocioRequest request) {
        SocioResponse socioCreado = socioService.crearSocio(request);
        return new ResponseEntity<>(socioCreado, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los socios", description = "Obtiene la lista completa de socios")
    public ResponseEntity<List<SocioResponse>> obtenerTodosLosSocios() {
        List<SocioResponse> socios = socioService.obtenerTodosLosSocios();
        return ResponseEntity.ok(socios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener socio por ID", description = "Obtiene un socio específico por su ID")
    public ResponseEntity<SocioResponse> obtenerSocioPorId(@PathVariable Long id) {
        SocioResponse socio = socioService.obtenerSocioPorId(id);
        return ResponseEntity.ok(socio);
    }

    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar socios por nombre", description = "Busca socios que contengan el nombre especificado")
    public ResponseEntity<List<SocioResponse>> buscarSociosPorNombre(@RequestParam String nombre) {
        List<SocioResponse> socios = socioService.buscarSociosPorNombre(nombre);
        return ResponseEntity.ok(socios);
    }

    @GetMapping("/numero/{nroSocio}")
    @Operation(summary = "Obtener socio por número", description = "Obtiene un socio por su número de socio")
    public ResponseEntity<SocioResponse> obtenerSocioPorNumero(@PathVariable Integer nroSocio) {
        SocioResponse socio = socioService.obtenerSocioPorNumero(nroSocio);
        return ResponseEntity.ok(socio);
    }

    @GetMapping("/dni/{dni}")
    @Operation(summary = "Obtener socio por DNI", description = "Obtiene un socio por su DNI")
    public ResponseEntity<SocioResponse> obtenerSocioPorDni(@PathVariable String dni) {
        SocioResponse socio = socioService.obtenerSocioPorDni(dni);
        return ResponseEntity.ok(socio);
    }

    @GetMapping("/{id}/prestamos-activos")
    @Operation(summary = "Verificar préstamos activos", description = "Verifica si un socio tiene préstamos activos")
    public ResponseEntity<Boolean> tienePrestamosActivos(@PathVariable Long id) {
        boolean tienePrestamos = socioService.tienePrestamosActivos(id);
        return ResponseEntity.ok(tienePrestamos);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar socio", description = "Actualiza la información de un socio existente")
    public ResponseEntity<SocioResponse> actualizarSocio(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateSocioRequest request) {
        SocioResponse socioActualizado = socioService.actualizarSocio(id, request);
        return ResponseEntity.ok(socioActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar socio", description = "Elimina un socio del sistema")
    public ResponseEntity<Void> eliminarSocio(@PathVariable Long id) {
        socioService.eliminarSocio(id);
        return ResponseEntity.noContent().build();
    }
}