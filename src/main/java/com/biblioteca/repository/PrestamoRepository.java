package com.biblioteca.repository;

import com.biblioteca.model.entity.Prestamo;
import com.biblioteca.model.enums.EstadoPrestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findBySocioIdSocio(Long idSocio);

    List<Prestamo> findByLibroIdLibro(Long idLibro);

    List<Prestamo> findByEstado(EstadoPrestamo estado);

    @Query("SELECT p FROM Prestamo p WHERE p.socio.idSocio = :idSocio AND p.estado = 'ACTIVO'")
    List<Prestamo> findPrestamosActivosBySocioId(@Param("idSocio") Long idSocio);

    @Query("SELECT p FROM Prestamo p WHERE p.estado IN ('ACTIVO', 'VENCIDO') AND p.fechaInicio < :fechaLimite")
    List<Prestamo> findPrestamosConRetraso(@Param("fechaLimite") LocalDate fechaLimite);

    @Query("SELECT COUNT(p) > 0 FROM Prestamo p WHERE p.libro.idLibro = :idLibro AND p.estado = 'ACTIVO'")
    boolean isLibroPrestado(@Param("idLibro") Long idLibro);

    List<Prestamo> findByFechaInicioBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<Prestamo> findBySocioIdSocioAndEstado(Long idSocio, EstadoPrestamo estado);

    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.socio.idSocio = :idSocio AND p.estado = 'ACTIVO'")
    Long countPrestamosActivosBySocioId(@Param("idSocio") Long idSocio);
}