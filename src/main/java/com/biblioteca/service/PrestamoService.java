package com.biblioteca.service;

import com.biblioteca.dto.request.CreatePrestamoRequest;
import com.biblioteca.dto.request.DevolucionPrestamoRequest;
import com.biblioteca.dto.response.PrestamoResponse;
import com.biblioteca.model.enums.EstadoPrestamo;

import java.util.List;

public interface PrestamoService {
    
    PrestamoResponse crearPrestamo(CreatePrestamoRequest request);
    PrestamoResponse obtenerPrestamoPorId(Long id);
    List<PrestamoResponse> obtenerTodosLosPrestamos();
    List<PrestamoResponse> obtenerPrestamosActivosPorSocio(Long idSocio);
    List<PrestamoResponse> obtenerPrestamosActivos();
    PrestamoResponse devolverPrestamo(Long idPrestamo, DevolucionPrestamoRequest request);
    List<PrestamoResponse> obtenerPrestamosConRetraso();
    boolean estaLibroPrestado(Long idLibro);
    List<PrestamoResponse> obtenerHistorialPorSocio(Long idSocio);
    
    void actualizarEstadosPrestamosVencidos();
    List<PrestamoResponse> obtenerPrestamosPorEstado(EstadoPrestamo estado);
}