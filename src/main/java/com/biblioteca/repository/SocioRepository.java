package com.biblioteca.repository;

import com.biblioteca.model.entity.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SocioRepository extends JpaRepository<Socio, Long> {

    Optional<Socio> findByNroSocio(Integer nroSocio);

    Optional<Socio> findByDni(String dni);

    boolean existsByNroSocio(Integer nroSocio);

    boolean existsByDni(String dni);

    List<Socio> findByNombreContainingIgnoreCase(String nombre);

    @Query("SELECT DISTINCT s FROM Socio s JOIN s.prestamos p WHERE p.estado = 'ACTIVO'")
    List<Socio> findSociosConPrestamosActivos();

    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.socio.idSocio = :idSocio AND p.estado = 'ACTIVO'")
    Long countPrestamosActivosBySocioId(@Param("idSocio") Long idSocio);
}