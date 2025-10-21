package com.biblioteca.service;

import com.biblioteca.dto.request.CreateMultaRequest;
import com.biblioteca.dto.response.MultaResponse;

import java.util.List;

public interface MultaService {

    MultaResponse generarMultaPorPrestamo(Long idPrestamo);
    MultaResponse obtenerMultaPorId(Long id);
    List<MultaResponse> obtenerTodasLasMultas();
    List<MultaResponse> obtenerMultasActivasPorSocio(Long idSocio);
    List<MultaResponse> obtenerMultasActivas();
    MultaResponse pagarMulta(Long idMulta);
    Double calcularTotalMultasPendientes(Long idSocio);
    boolean tieneMultasPendientes(Long idSocio);
    MultaResponse crearMultaManual(CreateMultaRequest request);
}