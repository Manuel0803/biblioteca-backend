package com.biblioteca.strategy;

import com.biblioteca.model.entity.Prestamo;
import java.math.BigDecimal;

public interface MultaStrategy {
    boolean aplica(Prestamo prestamo, int diasRetraso);
    BigDecimal calcularMonto(Prestamo prestamo, int diasRetraso);
    String obtenerMotivo(int diasRetraso);
}