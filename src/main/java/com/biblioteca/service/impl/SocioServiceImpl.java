package com.biblioteca.service.impl;

import com.biblioteca.dto.request.CreateSocioRequest;
import com.biblioteca.dto.request.UpdateSocioRequest;
import com.biblioteca.dto.response.SocioResponse;
import com.biblioteca.exception.OperationNotAllowedException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.entity.Socio;
import com.biblioteca.repository.SocioRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.service.SocioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SocioServiceImpl implements SocioService {

    private static final Logger logger = LoggerFactory.getLogger(SocioServiceImpl.class);
    private final SocioRepository socioRepository;
    private final PrestamoRepository prestamoRepository;

    public SocioServiceImpl(SocioRepository socioRepository, PrestamoRepository prestamoRepository) {
        this.socioRepository = socioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    @Override
    @Transactional
    public SocioResponse crearSocio(CreateSocioRequest request) {
        logger.info("Creando nuevo socio: {}", request);

        if (socioRepository.existsByNroSocio(request.getNroSocio())) {
            throw new OperationNotAllowedException("Ya existe un socio con el número: " + request.getNroSocio());
        }

        if (socioRepository.existsByDni(request.getDni())) {
            throw new OperationNotAllowedException("Ya existe un socio con el DNI: " + request.getDni());
        }

        Socio socio = new Socio();
        socio.setNombre(request.getNombre());
        socio.setNroSocio(request.getNroSocio());
        socio.setDni(request.getDni());

        Socio socioGuardado = socioRepository.save(socio);
        logger.info("Socio creado exitosamente con ID: {}", socioGuardado.getIdSocio());

        return convertirAResponse(socioGuardado);
    }

    @Override
    @Transactional
    public SocioResponse actualizarSocio(Long id, UpdateSocioRequest request) {
        logger.info("Actualizando socio ID: {} con datos: {}", id, request);
        
        Socio socioExistente = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + id));

        if (!socioExistente.getNroSocio().equals(request.getNroSocio()) && 
            socioRepository.existsByNroSocio(request.getNroSocio())) {
            throw new OperationNotAllowedException("Ya existe un socio con el número: " + request.getNroSocio());
        }

        socioExistente.setNombre(request.getNombre());
        socioExistente.setNroSocio(request.getNroSocio());
        
        Socio socioActualizado = socioRepository.save(socioExistente);
        logger.info("Socio actualizado exitosamente con ID: {}", id);
        
        return convertirAResponse(socioActualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponse obtenerSocioPorId(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + id));
        return convertirAResponse(socio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioResponse> obtenerTodosLosSocios() {
        List<SocioResponse> socios = socioRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
        
        logger.info("=== DEBUG SOCIOS ===");
        for (SocioResponse socio : socios) {
            logger.info("Socio: {} (ID: {}) - Préstamos activos: {}", 
                       socio.getNombre(), socio.getIdSocio(), socio.getPrestamosActivos());
        }
        logger.info("=== FIN DEBUG ===");
        
        return socios;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SocioResponse> buscarSociosPorNombre(String nombre) {
        return socioRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponse obtenerSocioPorNumero(Integer nroSocio) {
        Socio socio = socioRepository.findByNroSocio(nroSocio)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con número: " + nroSocio));
        return convertirAResponse(socio);
    }

    @Override
    @Transactional(readOnly = true)
    public SocioResponse obtenerSocioPorDni(String dni) {
        Socio socio = socioRepository.findByDni(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con DNI: " + dni));
        return convertirAResponse(socio);
    }

    @Override
    @Transactional
    public void eliminarSocio(Long id) {
        Socio socio = socioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + id));

        if (tienePrestamosActivos(id)) {
            throw new OperationNotAllowedException("No se puede eliminar un socio con préstamos activos");
        }

        socioRepository.delete(socio);
        logger.info("Socio eliminado exitosamente con ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tienePrestamosActivos(Long idSocio) {
        try {
            Long count = socioRepository.countPrestamosActivosBySocioId(idSocio);
            boolean tienePrestamos = count != null && count > 0;
            logger.info("Verificación préstamos activos - Socio ID: {} -> {} (count: {})", 
                       idSocio, tienePrestamos, count);
            return tienePrestamos;
        } catch (Exception e) {
            logger.error("Error al verificar préstamos activos para socio ID {}: {}", idSocio, e.getMessage());
            return false;
        }
    }

    private SocioResponse convertirAResponse(Socio socio) {
        SocioResponse response = new SocioResponse();
        response.setIdSocio(socio.getIdSocio());
        response.setNombre(socio.getNombre());
        response.setNroSocio(socio.getNroSocio());
        response.setDni(socio.getDni());
        
        try {
            Long countFromSocioRepo = socioRepository.countPrestamosActivosBySocioId(socio.getIdSocio());
            logger.debug("Método 1 - SocioRepository count: {} para socio ID: {}", 
                        countFromSocioRepo, socio.getIdSocio());
            
            int countFromPrestamoRepo = prestamoRepository.findPrestamosActivosBySocioId(socio.getIdSocio()).size();
            logger.debug("Método 2 - PrestamoRepository count: {} para socio ID: {}", 
                        countFromPrestamoRepo, socio.getIdSocio());
            
            int prestamosActivos = countFromSocioRepo != null ? countFromSocioRepo.intValue() : 0;
            response.setPrestamosActivos(prestamosActivos);
            
            if (prestamosActivos > 0) {
                logger.info("🎉 SOCIO CON PRÉSTAMOS: '{}' (ID: {}) tiene {} préstamos activos", 
                           socio.getNombre(), socio.getIdSocio(), prestamosActivos);
            }
            
        } catch (Exception e) {
            logger.error("❌ ERROR en convertirAResponse para socio ID {}: {}", 
                        socio.getIdSocio(), e.getMessage());
            response.setPrestamosActivos(0);
        }
        
        return response;
    }
}