package com.biblioteca.strategy;

import com.biblioteca.model.entity.Prestamo;
import java.math.BigDecimal;

public class MultaPorRetraso implements MultaStrategy {
    
    private static final BigDecimal MULTA_POR_DIA = new BigDecimal("10.00");
    private static final int DIAS_GRACIA = 2;

    @Override
    public boolean aplica(Prestamo prestamo, int diasRetraso) {
        return diasRetraso > DIAS_GRACIA && 
               prestamo.getEstadoDevolucion() == null;
    }

    @Override
    public BigDecimal calcularMonto(Prestamo prestamo, int diasRetraso) {
        if (diasRetraso <= DIAS_GRACIA) {
            return BigDecimal.ZERO;
        }
        int diasMultables = diasRetraso - DIAS_GRACIA;
        return MULTA_POR_DIA.multiply(new BigDecimal(diasMultables));
    }

    @Override
    public String obtenerMotivo(int diasRetraso) {
        if (diasRetraso <= DIAS_GRACIA) {
            return "Sin multa - dentro del período de gracia";
        }
        return String.format("Retraso en la devolución: %d días de retraso (%d días multables)", 
                           diasRetraso, diasRetraso - DIAS_GRACIA);
    }
}