package com.biblioteca.controller;

import com.biblioteca.dto.request.CreateMultaRequest;
import com.biblioteca.dto.response.MultaResponse;
import com.biblioteca.service.MultaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/multas")
@Tag(name = "Multas", description = "API para la gestión de multas")
public class MultaController {

    private final MultaService multaService;

    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }

    @PostMapping
    @Operation(summary = "Crear multa manualmente", description = "El bibliotecario crea una multa manualmente para un préstamo específico")
    public ResponseEntity<MultaResponse> crearMulta(@Valid @RequestBody CreateMultaRequest request) {
        MultaResponse multa = multaService.crearMultaManual(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(multa);
    }

    @PostMapping("/prestamo/{idPrestamo}/generar")
    @Operation(summary = "Generar multa por préstamo", description = "Genera una multa automáticamente para un préstamo")
    public ResponseEntity<MultaResponse> generarMultaPorPrestamo(@PathVariable Long idPrestamo) {
        MultaResponse multa = multaService.generarMultaPorPrestamo(idPrestamo);
        if (multa == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(multa);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las multas", description = "Obtiene la lista completa de multas")
    public ResponseEntity<List<MultaResponse>> obtenerTodasLasMultas() {
        List<MultaResponse> multas = multaService.obtenerTodasLasMultas();
        return ResponseEntity.ok(multas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener multa por ID", description = "Obtiene una multa específica por su ID")
    public ResponseEntity<MultaResponse> obtenerMultaPorId(@PathVariable Long id) {
        MultaResponse multa = multaService.obtenerMultaPorId(id);
        return ResponseEntity.ok(multa);
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener multas activas", description = "Obtiene todas las multas activas (no pagadas)")
    public ResponseEntity<List<MultaResponse>> obtenerMultasActivas() {
        List<MultaResponse> multas = multaService.obtenerMultasActivas();
        return ResponseEntity.ok(multas);
    }

    @GetMapping("/socio/{idSocio}/activas")
    @Operation(summary = "Obtener multas activas de socio", description = "Obtiene las multas activas de un socio específico")
    public ResponseEntity<List<MultaResponse>> obtenerMultasActivasPorSocio(@PathVariable Long idSocio) {
        List<MultaResponse> multas = multaService.obtenerMultasActivasPorSocio(idSocio);
        return ResponseEntity.ok(multas);
    }

    @GetMapping("/socio/{idSocio}/total-pendiente")
    @Operation(summary = "Calcular total de multas pendientes", description = "Calcula el monto total de multas pendientes de un socio")
    public ResponseEntity<Double> calcularTotalMultasPendientes(@PathVariable Long idSocio) {
        Double total = multaService.calcularTotalMultasPendientes(idSocio);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/socio/{idSocio}/tiene-pendientes")
    @Operation(summary = "Verificar multas pendientes", description = "Verifica si un socio tiene multas pendientes")
    public ResponseEntity<Boolean> tieneMultasPendientes(@PathVariable Long idSocio) {
        boolean tieneMultas = multaService.tieneMultasPendientes(idSocio);
        return ResponseEntity.ok(tieneMultas);
    }

    @PutMapping("/{idMulta}/pagar")
    @Operation(summary = "Pagar multa", description = "Marca una multa como pagada")
    public ResponseEntity<MultaResponse> pagarMulta(@PathVariable Long idMulta) {
        MultaResponse multa = multaService.pagarMulta(idMulta);
        return ResponseEntity.ok(multa);
    }
}