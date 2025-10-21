package com.biblioteca.strategy;

import com.biblioteca.model.entity.Prestamo;
import java.math.BigDecimal;

public class SinMulta implements MultaStrategy {

    @Override
    public boolean aplica(Prestamo prestamo, int diasRetraso) {
        return true;
    }

    @Override
    public BigDecimal calcularMonto(Prestamo prestamo, int diasRetraso) {
        return BigDecimal.ZERO;
    }

    @Override
    public String obtenerMotivo(int diasRetraso) {
        return "No se aplica multa - devolución en buen estado y sin retraso significativo";
    }
}