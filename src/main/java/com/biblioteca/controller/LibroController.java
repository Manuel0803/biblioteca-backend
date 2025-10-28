package com.biblioteca.controller;

import com.biblioteca.dto.request.CreateLibroRequest;
import com.biblioteca.dto.request.UpdateLibroRequest;
import com.biblioteca.dto.response.LibroResponse;
import com.biblioteca.model.enums.EstadoLibro;
import com.biblioteca.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = "API para la gestión de libros")
@CrossOrigin(origins = "https://biblioteca-frontend-host.vercel.app") 
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo libro", description = "Crea un nuevo libro en el sistema")
    public ResponseEntity<LibroResponse> crearLibro(@Valid @RequestBody CreateLibroRequest request) {
        LibroResponse libroCreado = libroService.crearLibro(request);
        return new ResponseEntity<>(libroCreado, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los libros", description = "Obtiene la lista completa de libros")
    public ResponseEntity<List<LibroResponse>> obtenerTodosLosLibros() {
        List<LibroResponse> libros = libroService.obtenerTodosLosLibros();
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener libro por ID", description = "Obtiene un libro específico por su ID")
    public ResponseEntity<LibroResponse> obtenerLibroPorId(@PathVariable Long id) {
        LibroResponse libro = libroService.obtenerLibroPorId(id);
        return ResponseEntity.ok(libro);
    }

    @GetMapping("/disponibles")
    @Operation(summary = "Obtener libros disponibles", description = "Obtiene todos los libros disponibles para préstamo")
    public ResponseEntity<List<LibroResponse>> obtenerLibrosDisponibles() {
        List<LibroResponse> libros = libroService.obtenerLibrosDisponibles();
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/buscar/titulo")
    @Operation(summary = "Buscar libros por título", description = "Busca libros que contengan el título especificado")
    public ResponseEntity<List<LibroResponse>> buscarLibrosPorTitulo(@RequestParam String titulo) {
        List<LibroResponse> libros = libroService.buscarLibrosPorTitulo(titulo);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/buscar/autor")
    @Operation(summary = "Buscar libros por autor", description = "Busca libros que contengan el autor especificado")
    public ResponseEntity<List<LibroResponse>> buscarLibrosPorAutor(@RequestParam String autor) {
        List<LibroResponse> libros = libroService.buscarLibrosPorAutor(autor);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener libros por estado", description = "Obtiene libros filtrados por estado")
    public ResponseEntity<List<LibroResponse>> obtenerLibrosPorEstado(@PathVariable EstadoLibro estado) {
        List<LibroResponse> libros = libroService.obtenerLibrosPorEstado(estado);
        return ResponseEntity.ok(libros);
    }

    @GetMapping("/{id}/disponible")
    @Operation(summary = "Verificar disponibilidad", description = "Verifica si un libro está disponible para préstamo")
    public ResponseEntity<Boolean> estaDisponible(@PathVariable Long id) {
        boolean disponible = libroService.estaDisponible(id);
        return ResponseEntity.ok(disponible);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar libro", description = "Actualiza la información de un libro existente")
    public ResponseEntity<LibroResponse> actualizarLibro(
            @PathVariable Long id, 
            @Valid @RequestBody UpdateLibroRequest request) {
        LibroResponse libroActualizado = libroService.actualizarLibro(id, request);
        return ResponseEntity.ok(libroActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar libro", description = "Elimina un libro del sistema")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        libroService.eliminarLibro(id);
        return ResponseEntity.noContent().build();
    }
}