package com.biblioteca.service;

import com.biblioteca.dto.request.CreateSocioRequest;
import com.biblioteca.dto.request.UpdateSocioRequest;
import com.biblioteca.dto.response.SocioResponse;

import java.util.List;

public interface SocioService {
    SocioResponse actualizarSocio(Long id, UpdateSocioRequest request);
    SocioResponse crearSocio(CreateSocioRequest request);
    SocioResponse obtenerSocioPorId(Long id);
    List<SocioResponse> obtenerTodosLosSocios();
    List<SocioResponse> buscarSociosPorNombre(String nombre);
    SocioResponse obtenerSocioPorNumero(Integer nroSocio);
    SocioResponse obtenerSocioPorDni(String dni);
    void eliminarSocio(Long id);
    boolean tienePrestamosActivos(Long idSocio);
}