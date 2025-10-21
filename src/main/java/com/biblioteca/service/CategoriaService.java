package com.biblioteca.service;

import com.biblioteca.dto.request.CreateCategoriaRequest;
import com.biblioteca.dto.request.UpdateCategoriaRequest;
import com.biblioteca.dto.response.CategoriaResponse;

import java.util.List;

public interface CategoriaService {

    List<CategoriaResponse> obtenerTodasLasCategorias();
    CategoriaResponse obtenerCategoriaPorId(Long id);
    CategoriaResponse obtenerCategoriaPorNombre(String nombre);
    List<CategoriaResponse> buscarCategoriasPorNombre(String nombre);
    
    CategoriaResponse crearCategoria(CreateCategoriaRequest request);
    CategoriaResponse actualizarCategoria(Long id, UpdateCategoriaRequest request);
    void eliminarCategoria(Long id);
}