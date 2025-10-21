package com.biblioteca.repository;

import com.biblioteca.model.entity.Libro;
import com.biblioteca.model.enums.EstadoLibro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByIsbn(String isbn);

    List<Libro> findByAutorContainingIgnoreCase(String autor);

    List<Libro> findByTituloContainingIgnoreCase(String titulo);

    List<Libro> findByEstado(EstadoLibro estado);

    @Query("SELECT l FROM Libro l WHERE l.estado = 'DISPONIBLE'")
    List<Libro> findLibrosDisponibles();

    @Query("SELECT l FROM Libro l WHERE l.estado = 'PRESTADO'")
    List<Libro> findLibrosPrestados();

    boolean existsByIsbn(String isbn);

    List<Libro> findByTituloContainingIgnoreCaseAndAutorContainingIgnoreCase(String titulo, String autor);

    Long countByCategoriaIdCategoria(Long idCategoria);
}