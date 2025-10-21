package com.biblioteca.service.impl;

import com.biblioteca.dto.request.CreateMultaRequest;
import com.biblioteca.dto.response.MultaResponse;
import com.biblioteca.dto.response.PrestamoResponse;
import com.biblioteca.exception.OperationNotAllowedException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.entity.Multa;
import com.biblioteca.model.entity.Prestamo;
import com.biblioteca.repository.MultaRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.service.MultaService;
import com.biblioteca.strategy.MultaStrategy;
import com.biblioteca.strategy.MultaPorRetraso;
import com.biblioteca.strategy.MultaPorDanio;
import com.biblioteca.strategy.SinMulta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MultaServiceImpl implements MultaService {

    private static final Logger logger = LoggerFactory.getLogger(MultaServiceImpl.class);
    private final MultaRepository multaRepository;
    private final PrestamoRepository prestamoRepository;
    private final List<MultaStrategy> estrategiasMulta;

    public MultaServiceImpl(MultaRepository multaRepository, PrestamoRepository prestamoRepository) {
        this.multaRepository = multaRepository;
        this.prestamoRepository = prestamoRepository;
        this.estrategiasMulta = new ArrayList<>();
        this.estrategiasMulta.add(new MultaPorRetraso());
        this.estrategiasMulta.add(new MultaPorDanio());
        this.estrategiasMulta.add(new SinMulta());
    }

    @Override
    @Transactional
    public MultaResponse crearMultaManual(CreateMultaRequest request) {
        logger.info("Creando multa manual: {}", request);
        
        Prestamo prestamo = prestamoRepository.findById(request.getPrestamoId())
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + request.getPrestamoId()));

        if (prestamo.tieneMulta()) {
            throw new OperationNotAllowedException("Ya existe una multa para este préstamo (ID Multa: " + 
                                                  prestamo.getMulta().getIdMulta() + ")");
        }

        if (prestamo.estaActivo()) {
            throw new OperationNotAllowedException("No se puede crear multa para un préstamo activo");
        }

        if (!prestamo.estaFinalizado()) {
            throw new OperationNotAllowedException("El préstamo no está finalizado. Estado actual: " + prestamo.getEstado());
        }

        Multa multa = new Multa();
        multa.setMonto(request.getMonto());
        multa.setMotivo(request.getMotivo() + " (Multa manual)");
        multa.setPrestamo(prestamo);
        multa.setPagada(false);

        Multa multaGuardada = multaRepository.save(multa);
        prestamo.setMulta(multaGuardada);
        prestamoRepository.save(prestamo);

        logger.info("Multa manual creada exitosamente con ID: {} para préstamo ID: {}. Monto: ${}", 
                   multaGuardada.getIdMulta(), request.getPrestamoId(), request.getMonto());

        return convertirAResponse(multaGuardada);
    }

    @Override
    @Transactional
    public MultaResponse generarMultaPorPrestamo(Long idPrestamo) {
        logger.info("Generando multa para préstamo ID: {}", idPrestamo);
        
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + idPrestamo));

        if (prestamo.estaActivo()) {
            throw new OperationNotAllowedException("No se puede generar multa para un préstamo activo");
        }

        if (prestamo.tieneMulta()) {
            throw new OperationNotAllowedException("Ya existe una multa para este préstamo");
        }

        int diasRetraso = prestamo.calcularDiasRetraso();
        MultaStrategy estrategiaAplicable = determinarEstrategiaAplicable(prestamo, diasRetraso);
        BigDecimal monto = estrategiaAplicable.calcularMonto(prestamo, diasRetraso);
        String motivo = estrategiaAplicable.obtenerMotivo(diasRetraso);

        if (monto.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("No se genera multa para préstamo ID: {} - Motivo: {}", idPrestamo, motivo);
            return null;
        }

        Multa multa = new Multa();
        multa.setMonto(monto);
        multa.setMotivo(motivo);
        multa.setPrestamo(prestamo);
        multa.setPagada(false);

        Multa multaGuardada = multaRepository.save(multa);
        prestamo.setMulta(multaGuardada);
        prestamoRepository.save(prestamo);

        logger.info("Multa generada exitosamente con ID: {} para préstamo ID: {}", 
                   multaGuardada.getIdMulta(), idPrestamo);

        return convertirAResponse(multaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public MultaResponse obtenerMultaPorId(Long id) {
        logger.debug("Buscando multa por ID: {}", id);
        Multa multa = multaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Multa no encontrada con ID: " + id));
        return convertirAResponse(multa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MultaResponse> obtenerTodasLasMultas() {
        logger.debug("Obteniendo todas las multas");
        return multaRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MultaResponse> obtenerMultasActivasPorSocio(Long idSocio) {
        logger.debug("Obteniendo multas activas para socio ID: {}", idSocio);
        return multaRepository.findMultasActivasBySocioId(idSocio).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MultaResponse> obtenerMultasActivas() {
        logger.debug("Obteniendo todas las multas activas");
        return multaRepository.findByPagadaFalse().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MultaResponse pagarMulta(Long idMulta) {
        logger.info("Marcando multa ID: {} como pagada", idMulta);
        
        Multa multa = multaRepository.findById(idMulta)
                .orElseThrow(() -> new ResourceNotFoundException("Multa no encontrada con ID: " + idMulta));

        if (multa.getPagada()) {
            throw new OperationNotAllowedException("La multa ya está pagada");
        }

        multa.marcarComoPagada();
        Multa multaActualizada = multaRepository.save(multa);
        
        logger.info("Multa ID: {} marcada como pagada", idMulta);
        return convertirAResponse(multaActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public Double calcularTotalMultasPendientes(Long idSocio) {
        logger.debug("Calculando total de multas pendientes para socio ID: {}", idSocio);
        BigDecimal total = multaRepository.sumMultasActivasBySocioId(idSocio);
        return total != null ? total.doubleValue() : 0.0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneMultasPendientes(Long idSocio) {
        logger.debug("Verificando si socio ID: {} tiene multas pendientes", idSocio);
        BigDecimal total = multaRepository.sumMultasActivasBySocioId(idSocio);
        return total != null && total.compareTo(BigDecimal.ZERO) > 0;
    }

    private MultaStrategy determinarEstrategiaAplicable(Prestamo prestamo, int diasRetraso) {
        for (MultaStrategy estrategia : estrategiasMulta) {
            if (estrategia.aplica(prestamo, diasRetraso)) {
                logger.debug("Estrategia aplicable: {}", estrategia.getClass().getSimpleName());
                return estrategia;
            }
        }
        return new SinMulta();
    }

    private MultaResponse convertirAResponse(Multa multa) {
        MultaResponse response = new MultaResponse();
        response.setIdMulta(multa.getIdMulta());
        response.setMonto(multa.getMonto());
        response.setMotivo(multa.getMotivo());
        response.setPagada(multa.getPagada());
        response.setActiva(multa.estaActiva());

        if (multa.getPrestamo() != null) {
            PrestamoResponse prestamoResponse = new PrestamoResponse();
            prestamoResponse.setIdPrestamo(multa.getPrestamo().getIdPrestamo());
            prestamoResponse.setFechaInicio(multa.getPrestamo().getFechaInicio());
            prestamoResponse.setFechaFinPrevista(multa.getPrestamo().getFechaFinPrevista());
            prestamoResponse.setFechaDevolucionReal(multa.getPrestamo().getFechaDevolucionReal());
            prestamoResponse.setEstado(multa.getPrestamo().getEstado());
            prestamoResponse.setActivo(multa.getPrestamo().estaActivo());
            prestamoResponse.setDiasRetraso(multa.getPrestamo().calcularDiasRetraso());
            prestamoResponse.setTieneMulta(multa.getPrestamo().tieneMulta());
            
            if (multa.getPrestamo().getSocio() != null) {
                PrestamoResponse.SocioInfo socioInfo = new PrestamoResponse.SocioInfo();
                socioInfo.setIdSocio(multa.getPrestamo().getSocio().getIdSocio());
                socioInfo.setNombre(multa.getPrestamo().getSocio().getNombre());
                socioInfo.setNroSocio(multa.getPrestamo().getSocio().getNroSocio());
                prestamoResponse.setSocioInfo(socioInfo);
            }
            
            response.setPrestamo(prestamoResponse);
        }

        return response;
    }
}