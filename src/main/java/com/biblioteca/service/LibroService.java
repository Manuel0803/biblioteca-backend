package com.biblioteca.service;

import com.biblioteca.dto.request.CreateLibroRequest;
import com.biblioteca.dto.request.UpdateLibroRequest;
import com.biblioteca.dto.response.LibroResponse;
import com.biblioteca.model.enums.EstadoLibro;

import java.util.List;

public interface LibroService {
    LibroResponse actualizarLibro(Long id, UpdateLibroRequest request);
    LibroResponse crearLibro(CreateLibroRequest request);
    LibroResponse obtenerLibroPorId(Long id);
    List<LibroResponse> obtenerTodosLosLibros();
    List<LibroResponse> buscarLibrosPorTitulo(String titulo);
    List<LibroResponse> buscarLibrosPorAutor(String autor);
    List<LibroResponse> obtenerLibrosPorEstado(EstadoLibro estado);
    List<LibroResponse> obtenerLibrosDisponibles();
    boolean estaDisponible(Long idLibro);
    void eliminarLibro(Long id);
}