package com.biblioteca.service.impl;

import com.biblioteca.dto.request.CreateLibroRequest;
import com.biblioteca.dto.request.UpdateLibroRequest;
import com.biblioteca.dto.response.LibroResponse;
import com.biblioteca.exception.OperationNotAllowedException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.entity.Libro;
import com.biblioteca.model.entity.Categoria;
import com.biblioteca.model.enums.EstadoLibro;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.CategoriaRepository;
import com.biblioteca.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LibroServiceImpl implements LibroService {

    private static final Logger logger = LoggerFactory.getLogger(LibroServiceImpl.class);
    private final LibroRepository libroRepository;
    private final CategoriaRepository categoriaRepository;

    public LibroServiceImpl(LibroRepository libroRepository, CategoriaRepository categoriaRepository) {
        this.libroRepository = libroRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    @Transactional
    public LibroResponse crearLibro(CreateLibroRequest request) {
        logger.info("Creando nuevo libro: {}", request);

        if (libroRepository.existsByIsbn(request.getIsbn())) {
            throw new OperationNotAllowedException("Ya existe un libro con el ISBN: " + request.getIsbn());
        }

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + request.getIdCategoria()));

        Libro libro = new Libro();
        libro.setTitulo(request.getTitulo());
        libro.setAutor(request.getAutor());
        libro.setIsbn(request.getIsbn());
        libro.setEstado(EstadoLibro.DISPONIBLE);
        libro.setCategoria(categoria);

        Libro libroGuardado = libroRepository.save(libro);
        logger.info("Libro creado exitosamente con ID: {}", libroGuardado.getIdLibro());

        return convertirAResponse(libroGuardado);
    }

    @Override
    @Transactional
    public LibroResponse actualizarLibro(Long id, UpdateLibroRequest request) {
        logger.info("Actualizando libro ID: {} con datos: {}", id, request);
        
        Libro libroExistente = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));
        
        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con ID: " + request.getIdCategoria()));

        libroExistente.setTitulo(request.getTitulo());
        libroExistente.setAutor(request.getAutor());
        libroExistente.setCategoria(categoria);
        
        Libro libroActualizado = libroRepository.save(libroExistente);
        logger.info("Libro actualizado exitosamente con ID: {}", id);
        
        return convertirAResponse(libroActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public LibroResponse obtenerLibroPorId(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));
        return convertirAResponse(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> obtenerTodosLosLibros() {
        return libroRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> buscarLibrosPorTitulo(String titulo) {
        return libroRepository.findByTituloContainingIgnoreCase(titulo).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> buscarLibrosPorAutor(String autor) {
        return libroRepository.findByAutorContainingIgnoreCase(autor).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> obtenerLibrosPorEstado(EstadoLibro estado) {
        return libroRepository.findByEstado(estado).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<LibroResponse> obtenerLibrosDisponibles() {
        return libroRepository.findLibrosDisponibles().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estaDisponible(Long idLibro) {
        return libroRepository.findById(idLibro)
                .map(Libro::estaDisponible)
                .orElse(false);
    }

    @Override
    @Transactional
    public void eliminarLibro(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));

        if (!libro.estaDisponible()) {
            throw new OperationNotAllowedException("No se puede eliminar un libro que está prestado");
        }

        libroRepository.delete(libro);
        logger.info("Libro eliminado exitosamente con ID: {}", id);
    }

    private LibroResponse convertirAResponse(Libro libro) {
        LibroResponse response = new LibroResponse();
        response.setIdLibro(libro.getIdLibro());
        response.setTitulo(libro.getTitulo());
        response.setAutor(libro.getAutor());
        response.setIsbn(libro.getIsbn());
        response.setEstado(libro.getEstado());

        if (libro.getCategoria() != null) {
            LibroResponse.CategoriaInfo categoriaInfo = new LibroResponse.CategoriaInfo();
            categoriaInfo.setIdCategoria(libro.getCategoria().getIdCategoria());
            categoriaInfo.setNombre(libro.getCategoria().getNombre());
            categoriaInfo.setDescripcion(libro.getCategoria().getDescripcion());
            response.setCategoria(categoriaInfo);
        }

        return response;
    }
}