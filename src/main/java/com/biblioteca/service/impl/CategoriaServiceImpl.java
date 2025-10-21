package com.biblioteca.service.impl;

import com.biblioteca.dto.request.CreateCategoriaRequest;
import com.biblioteca.dto.request.UpdateCategoriaRequest;
import com.biblioteca.dto.response.CategoriaResponse;
import com.biblioteca.exception.OperationNotAllowedException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.entity.Categoria;
import com.biblioteca.repository.CategoriaRepository;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.service.CategoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaServiceImpl.class);
    private final CategoriaRepository categoriaRepository;
    private final LibroRepository libroRepository;

    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, LibroRepository libroRepository) {
        this.categoriaRepository = categoriaRepository;
        this.libroRepository = libroRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> obtenerTodasLasCategorias() {
        logger.debug("Obteniendo todas las categorías");
        return categoriaRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtenerCategoriaPorId(Long id) {
        logger.debug("Buscando categoría por ID: {}", id);
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));
        return convertirAResponse(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoriaResponse obtenerCategoriaPorNombre(String nombre) {
        logger.debug("Buscando categoría por nombre: {}", nombre);
        Categoria categoria = categoriaRepository.findByNombre(nombre)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con nombre: " + nombre));
        return convertirAResponse(categoria);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResponse> buscarCategoriasPorNombre(String nombre) {
        logger.debug("Buscando categorías por nombre: {}", nombre);
        return categoriaRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoriaResponse crearCategoria(CreateCategoriaRequest request) {
        logger.info("Creando nueva categoría: {}", request);

        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new OperationNotAllowedException("Ya existe una categoría con el nombre: " + request.getNombre());
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        categoria.setDescripcion(request.getDescripcion());

        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        logger.info("Categoría creada exitosamente con ID: {}", categoriaGuardada.getIdCategoria());

        return convertirAResponse(categoriaGuardada);
    }

    @Override
    @Transactional
    public CategoriaResponse actualizarCategoria(Long id, UpdateCategoriaRequest request) {
        logger.info("Actualizando categoría ID: {} con datos: {}", id, request);
        
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        if (!categoriaExistente.getNombre().equals(request.getNombre()) && 
            categoriaRepository.existsByNombre(request.getNombre())) {
            throw new OperationNotAllowedException("Ya existe una categoría con el nombre: " + request.getNombre());
        }

        categoriaExistente.setNombre(request.getNombre());
        categoriaExistente.setDescripcion(request.getDescripcion());

        Categoria categoriaActualizada = categoriaRepository.save(categoriaExistente);
        logger.info("Categoría actualizada exitosamente con ID: {}", id);
        
        return convertirAResponse(categoriaActualizada);
    }

    @Override
    @Transactional
    public void eliminarCategoria(Long id) {
        logger.info("Eliminando categoría ID: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + id));

        Long cantidadLibros = libroRepository.countByCategoriaIdCategoria(id);
        if (cantidadLibros != null && cantidadLibros > 0) {
            throw new OperationNotAllowedException(
                "No se puede eliminar la categoría porque tiene " + cantidadLibros + " libros asociados"
            );
        }

        categoriaRepository.delete(categoria);
        logger.info("Categoría eliminada exitosamente con ID: {}", id);
    }

    private CategoriaResponse convertirAResponse(Categoria categoria) {
        CategoriaResponse response = new CategoriaResponse();
        response.setIdCategoria(categoria.getIdCategoria());
        response.setNombre(categoria.getNombre());
        response.setDescripcion(categoria.getDescripcion());
        
        Long cantidadLibros = libroRepository.countByCategoriaIdCategoria(categoria.getIdCategoria());
        response.setCantidadLibros(cantidadLibros != null ? cantidadLibros.intValue() : 0);
        
        return response;
    }
}