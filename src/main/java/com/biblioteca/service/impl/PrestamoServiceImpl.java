package com.biblioteca.service.impl;

import com.biblioteca.dto.request.CreatePrestamoRequest;
import com.biblioteca.dto.request.DevolucionPrestamoRequest;
import com.biblioteca.dto.response.LibroResponse;
import com.biblioteca.dto.response.MultaResponse;
import com.biblioteca.dto.response.PrestamoResponse;
import com.biblioteca.dto.response.SocioResponse;
import com.biblioteca.exception.OperationNotAllowedException;
import com.biblioteca.exception.ResourceNotFoundException;
import com.biblioteca.model.entity.Libro;
import com.biblioteca.model.entity.Prestamo;
import com.biblioteca.model.entity.Socio;
import com.biblioteca.model.enums.EstadoPrestamo;
import com.biblioteca.repository.LibroRepository;
import com.biblioteca.repository.PrestamoRepository;
import com.biblioteca.repository.SocioRepository;
import com.biblioteca.service.MultaService;
import com.biblioteca.service.PrestamoService;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PrestamoServiceImpl implements PrestamoService {

    private static final Logger logger = LoggerFactory.getLogger(PrestamoServiceImpl.class);
    private static final int DIAS_PRESTAMO = 15;

    private final PrestamoRepository prestamoRepository;
    private final LibroRepository libroRepository;
    private final SocioRepository socioRepository;
    private final MultaService multaService;

    public PrestamoServiceImpl(PrestamoRepository prestamoRepository, 
                         LibroRepository libroRepository, 
                         SocioRepository socioRepository,
                         MultaService multaService) {
        this.prestamoRepository = prestamoRepository;
        this.libroRepository = libroRepository;
        this.socioRepository = socioRepository;
        this.multaService = multaService;
    }

    @Override
    @Transactional
    public PrestamoResponse crearPrestamo(CreatePrestamoRequest request) {
        logger.info("Creando nuevo préstamo: {}", request);

        Libro libro = libroRepository.findById(request.getIdLibro())
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + request.getIdLibro()));

        if (!libro.estaDisponible()) {
            throw new OperationNotAllowedException("El libro no está disponible para préstamo");
        }

        Socio socio = socioRepository.findById(request.getIdSocio())
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con ID: " + request.getIdSocio()));

        Prestamo prestamo = new Prestamo();
        prestamo.setLibro(libro);
        prestamo.setSocio(socio);
        prestamo.setEstado(EstadoPrestamo.ACTIVO);

        if (request.getFechaInicio() != null) {
            prestamo.setFechaInicio(request.getFechaInicio());
        }

        prestamo.setFechaFinPrevista(request.getFechaFin());
        libro.marcarComoPrestado();
        Prestamo prestamoGuardado = prestamoRepository.save(prestamo);
        
        logger.info("Préstamo creado exitosamente con ID: {}", prestamoGuardado.getIdPrestamo());
        return convertirAResponse(prestamoGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoResponse obtenerPrestamoPorId(Long id) {
        logger.debug("Buscando préstamo por ID: {}", id);
        Prestamo prestamo = prestamoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + id));
        return convertirAResponse(prestamo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> obtenerTodosLosPrestamos() {
        logger.debug("Obteniendo todos los préstamos");
        return prestamoRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> obtenerPrestamosActivosPorSocio(Long idSocio) {
        if (!socioRepository.existsById(idSocio)) {
            throw new ResourceNotFoundException("Socio no encontrado con ID: " + idSocio);
        }
        
        List<Prestamo> prestamosActivos = prestamoRepository.findPrestamosActivosBySocioId(idSocio);
        logger.info("Encontrados {} préstamos activos para socio ID: {}", prestamosActivos.size(), idSocio);
        
        return prestamosActivos.stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> obtenerPrestamosActivos() {
        logger.debug("Obteniendo todos los préstamos activos");
        return prestamoRepository.findByEstado(EstadoPrestamo.ACTIVO).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PrestamoResponse devolverPrestamo(Long idPrestamo, DevolucionPrestamoRequest request) {
        logger.info("Procesando devolución del préstamo ID: {}", idPrestamo);
        
        Prestamo prestamo = prestamoRepository.findById(idPrestamo)
                .orElseThrow(() -> new ResourceNotFoundException("Préstamo no encontrado con ID: " + idPrestamo));

        if (!prestamo.estaActivo()) {
            throw new OperationNotAllowedException("El préstamo ya ha sido devuelto o no está activo");
        }

        prestamo.setEstadoDevolucion(request.getEstadoDevolucion());
        prestamo.setObservacionesDevolucion(request.getObservaciones());
        prestamo.setTieneDanio(request.getEstadoDevolucion().isAplicaMulta());
        prestamo.finalizarPrestamo();

        Prestamo prestamoActualizado = prestamoRepository.save(prestamo);
        logger.info("Devolución procesada exitosamente para préstamo ID: {}", idPrestamo);

        try {
            generarMultaAutomaticaSiCorresponde(prestamoActualizado);
        } catch (Exception e) {
            logger.error("Error al generar multa automática para préstamo {}: {}", idPrestamo, e.getMessage());
        }

        return convertirAResponse(prestamoActualizado);
    }

    private void generarMultaAutomaticaSiCorresponde(Prestamo prestamo) {
        boolean debeGenerarMulta = false;
        
        if (prestamo.getEstadoDevolucion() != null && prestamo.getEstadoDevolucion().isAplicaMulta()) {
            debeGenerarMulta = true;
            logger.info("Multa por daño aplicada para préstamo ID: {} - Estado: {}", 
                       prestamo.getIdPrestamo(), prestamo.getEstadoDevolucion());
        }
        
        int diasRetraso = prestamo.calcularDiasRetraso();
        if (diasRetraso > 0) {
            debeGenerarMulta = true;
            logger.info("Multa por retraso aplicada para préstamo ID: {} ({} días)", 
                       prestamo.getIdPrestamo(), diasRetraso);
        }
        
        if (debeGenerarMulta && !prestamo.tieneMulta()) {
            try {
                MultaResponse multaGenerada = multaService.generarMultaPorPrestamo(prestamo.getIdPrestamo());
                if (multaGenerada != null) {
                    logger.info("Multa automática generada exitosamente con ID: {} para préstamo ID: {}", 
                               multaGenerada.getIdMulta(), prestamo.getIdPrestamo());
                } else {
                    logger.info("No se generó multa automática para préstamo ID: {}", prestamo.getIdPrestamo());
                }
            } catch (Exception e) {
                logger.error("Error al generar multa automática para préstamo {}: {}", 
                           prestamo.getIdPrestamo(), e.getMessage());
            }
        } else if (prestamo.tieneMulta()) {
            logger.info("Préstamo ID: {} ya tiene multa asociada, no se genera nueva", prestamo.getIdPrestamo());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> obtenerPrestamosConRetraso() {
        logger.debug("Obteniendo préstamos con retraso");
        LocalDate fechaLimite = LocalDate.now().minusDays(DIAS_PRESTAMO);
        return prestamoRepository.findPrestamosConRetraso(fechaLimite).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean estaLibroPrestado(Long idLibro) {
        logger.debug("Verificando si el libro ID: {} está prestado", idLibro);
        return prestamoRepository.isLibroPrestado(idLibro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> obtenerHistorialPorSocio(Long idSocio) {
        logger.debug("Obteniendo historial de préstamos para socio ID: {}", idSocio);
        
        if (!socioRepository.existsById(idSocio)) {
            throw new ResourceNotFoundException("Socio no encontrado con ID: " + idSocio);
        }
        
        return prestamoRepository.findBySocioIdSocio(idSocio).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void actualizarEstadosPrestamosVencidos() {
        logger.info("Actualizando estados de préstamos vencidos");
        
        List<Prestamo> prestamosActivos = prestamoRepository.findByEstado(EstadoPrestamo.ACTIVO);
        int contadorVencidos = 0;
        
        for (Prestamo prestamo : prestamosActivos) {
            if (prestamo.getFechaFinPrevista() != null && 
                LocalDate.now().isAfter(prestamo.getFechaFinPrevista())) {
                prestamo.setEstado(EstadoPrestamo.VENCIDO);
                contadorVencidos++;
            }
        }
        
        if (contadorVencidos > 0) {
            prestamoRepository.saveAll(prestamosActivos);
            logger.info("{} préstamos marcados como vencidos", contadorVencidos);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoResponse> obtenerPrestamosPorEstado(EstadoPrestamo estado) {
        logger.debug("Obteniendo préstamos con estado: {}", estado);
        return prestamoRepository.findByEstado(estado).stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    private PrestamoResponse convertirAResponse(Prestamo prestamo) {
        PrestamoResponse response = new PrestamoResponse();
        response.setIdPrestamo(prestamo.getIdPrestamo());
        response.setFechaInicio(prestamo.getFechaInicio());
        response.setFechaFinPrevista(prestamo.getFechaFinPrevista());
        response.setFechaDevolucionReal(prestamo.getFechaDevolucionReal());
        response.setEstado(prestamo.getEstado());
        response.setActivo(prestamo.estaActivo());
        response.setDiasRetraso(prestamo.calcularDiasRetraso());
        response.setTieneMulta(prestamo.tieneMulta());

        if (prestamo.getLibro() != null) {
            LibroResponse libroResponse = new LibroResponse();
            libroResponse.setIdLibro(prestamo.getLibro().getIdLibro());
            libroResponse.setTitulo(prestamo.getLibro().getTitulo());
            libroResponse.setAutor(prestamo.getLibro().getAutor());
            libroResponse.setIsbn(prestamo.getLibro().getIsbn());
            libroResponse.setEstado(prestamo.getLibro().getEstado());
            response.setLibro(libroResponse);
        }

        if (prestamo.getSocio() != null) {
            SocioResponse socioResponse = new SocioResponse();
            socioResponse.setIdSocio(prestamo.getSocio().getIdSocio());
            socioResponse.setNombre(prestamo.getSocio().getNombre());
            socioResponse.setNroSocio(prestamo.getSocio().getNroSocio());
            socioResponse.setDni(prestamo.getSocio().getDni());
            
            Long prestamosActivos = prestamoRepository.countPrestamosActivosBySocioId(prestamo.getSocio().getIdSocio());
            socioResponse.setPrestamosActivos(prestamosActivos != null ? prestamosActivos.intValue() : 0);
            
            response.setSocio(socioResponse);
        }

        return response;
    }
}