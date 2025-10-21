package com.biblioteca.controller;

import com.biblioteca.dto.request.CreateCategoriaRequest;
import com.biblioteca.dto.request.UpdateCategoriaRequest;
import com.biblioteca.dto.response.CategoriaResponse;
import com.biblioteca.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorías", description = "API para la gestión de categorías de libros")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría en el sistema")
    public ResponseEntity<CategoriaResponse> crearCategoria(@Valid @RequestBody CreateCategoriaRequest request) {
        CategoriaResponse categoriaCreada = categoriaService.crearCategoria(request);
        return new ResponseEntity<>(categoriaCreada, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las categorías", description = "Obtiene la lista completa de categorías")
    public ResponseEntity<List<CategoriaResponse>> obtenerTodasLasCategorias() {
        List<CategoriaResponse> categorias = categoriaService.obtenerTodasLasCategorias();
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Obtiene una categoría específica por su ID")
    public ResponseEntity<CategoriaResponse> obtenerCategoriaPorId(@PathVariable Long id) {
        CategoriaResponse categoria = categoriaService.obtenerCategoriaPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener categoría por nombre", description = "Obtiene una categoría por su nombre exacto")
    public ResponseEntity<CategoriaResponse> obtenerCategoriaPorNombre(@PathVariable String nombre) {
        CategoriaResponse categoria = categoriaService.obtenerCategoriaPorNombre(nombre);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar categorías por nombre", description = "Busca categorías que contengan el nombre especificado")
    public ResponseEntity<List<CategoriaResponse>> buscarCategoriasPorNombre(@RequestParam String nombre) {
        List<CategoriaResponse> categorias = categoriaService.buscarCategoriasPorNombre(nombre);
        return ResponseEntity.ok(categorias);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente")
    public ResponseEntity<CategoriaResponse> actualizarCategoria(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateCategoriaRequest request) {
        CategoriaResponse categoriaActualizada = categoriaService.actualizarCategoria(id, request);
        return ResponseEntity.ok(categoriaActualizada);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}