package com.biblioteca.strategy;

import com.biblioteca.model.entity.Prestamo;
import java.math.BigDecimal;

public class MultaPorDanio implements MultaStrategy {
    
    private static final BigDecimal MULTA_DANIO_LEVE = new BigDecimal("50.00");
    private static final BigDecimal MULTA_DANIO_GRAVE = new BigDecimal("150.00");
    private static final BigDecimal MULTA_PERDIDA = new BigDecimal("500.00");

    @Override
    public boolean aplica(Prestamo prestamo, int diasRetraso) {
        return prestamo.getEstadoDevolucion() != null && 
               prestamo.getEstadoDevolucion().isAplicaMulta();
    }

    @Override
    public BigDecimal calcularMonto(Prestamo prestamo, int diasRetraso) {
        if (prestamo.getEstadoDevolucion() == null) {
            return BigDecimal.ZERO;
        }
        
        return switch (prestamo.getEstadoDevolucion()) {
            case DANIO_LEVE -> MULTA_DANIO_LEVE;
            case DANIO_GRAVE -> MULTA_DANIO_GRAVE;
            case PERDIDA -> MULTA_PERDIDA;
            default -> BigDecimal.ZERO;
        };
    }

    @Override
    public String obtenerMotivo(int diasRetraso) {
        return "Multa por daño o pérdida del libro";
    }
}