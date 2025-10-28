package com.biblioteca.controller;

import com.biblioteca.dto.request.CreatePrestamoRequest;
import com.biblioteca.dto.request.DevolucionPrestamoRequest;
import com.biblioteca.dto.response.PrestamoResponse;
import com.biblioteca.service.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamos")
@Tag(name = "Préstamos", description = "API para la gestión de préstamos")
@CrossOrigin(origins = "https://biblioteca-frontend-host.vercel.app")
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo préstamo", description = "Registra un nuevo préstamo en el sistema")
    public ResponseEntity<PrestamoResponse> crearPrestamo(@Valid @RequestBody CreatePrestamoRequest request) {
        PrestamoResponse prestamoCreado = prestamoService.crearPrestamo(request);
        return new ResponseEntity<>(prestamoCreado, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los préstamos", description = "Obtiene la lista completa de préstamos")
    public ResponseEntity<List<PrestamoResponse>> obtenerTodosLosPrestamos() {
        List<PrestamoResponse> prestamos = prestamoService.obtenerTodosLosPrestamos();
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener préstamo por ID", description = "Obtiene un préstamo específico por su ID")
    public ResponseEntity<PrestamoResponse> obtenerPrestamoPorId(@PathVariable Long id) {
        PrestamoResponse prestamo = prestamoService.obtenerPrestamoPorId(id);
        return ResponseEntity.ok(prestamo);
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener préstamos activos", description = "Obtiene todos los préstamos activos en el sistema")
    public ResponseEntity<List<PrestamoResponse>> obtenerPrestamosActivos() {
        List<PrestamoResponse> prestamos = prestamoService.obtenerPrestamosActivos();
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/socio/{idSocio}/activos")
    @Operation(summary = "Obtener préstamos activos de socio", description = "Obtiene los préstamos activos de un socio específico")
    public ResponseEntity<List<PrestamoResponse>> obtenerPrestamosActivosPorSocio(@PathVariable Long idSocio) {
        List<PrestamoResponse> prestamos = prestamoService.obtenerPrestamosActivosPorSocio(idSocio);
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/socio/{idSocio}/historial")
    @Operation(summary = "Obtener historial de préstamos", description = "Obtiene el historial completo de préstamos de un socio")
    public ResponseEntity<List<PrestamoResponse>> obtenerHistorialPorSocio(@PathVariable Long idSocio) {
        List<PrestamoResponse> prestamos = prestamoService.obtenerHistorialPorSocio(idSocio);
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/retraso")
    @Operation(summary = "Obtener préstamos con retraso", description = "Obtiene los préstamos que tienen retraso en la devolución")
    public ResponseEntity<List<PrestamoResponse>> obtenerPrestamosConRetraso() {
        List<PrestamoResponse> prestamos = prestamoService.obtenerPrestamosConRetraso();
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/libro/{idLibro}/prestado")
    @Operation(summary = "Verificar libro prestado", description = "Verifica si un libro está actualmente prestado")
    public ResponseEntity<Boolean> estaLibroPrestado(@PathVariable Long idLibro) {
        boolean prestado = prestamoService.estaLibroPrestado(idLibro);
        return ResponseEntity.ok(prestado);
    }

    @PutMapping("/{idPrestamo}/devolucion")
    @Operation(summary = "Registrar devolución", description = "Registra la devolución de un préstamo")
    public ResponseEntity<PrestamoResponse> devolverPrestamo(
            @PathVariable Long idPrestamo,
            @Valid @RequestBody DevolucionPrestamoRequest request) {
        PrestamoResponse prestamo = prestamoService.devolverPrestamo(idPrestamo, request);
        return ResponseEntity.ok(prestamo);
    }
}