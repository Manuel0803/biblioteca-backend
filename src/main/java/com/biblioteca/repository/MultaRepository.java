package com.biblioteca.repository;

import com.biblioteca.model.entity.Multa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MultaRepository extends JpaRepository<Multa, Long> {

    Optional<Multa> findByPrestamoIdPrestamo(Long idPrestamo);

    List<Multa> findByPagadaFalse();

    List<Multa> findByPagadaTrue();

    @Query("SELECT m FROM Multa m WHERE m.prestamo.socio.idSocio = :idSocio")
    List<Multa> findBySocioId(@Param("idSocio") Long idSocio);

    @Query("SELECT m FROM Multa m WHERE m.prestamo.socio.idSocio = :idSocio AND m.pagada = false")
    List<Multa> findMultasActivasBySocioId(@Param("idSocio") Long idSocio);

    @Query("SELECT COALESCE(SUM(m.monto), 0) FROM Multa m WHERE m.prestamo.socio.idSocio = :idSocio AND m.pagada = false")
    BigDecimal sumMultasActivasBySocioId(@Param("idSocio") Long idSocio);
}