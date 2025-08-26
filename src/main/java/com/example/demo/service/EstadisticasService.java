package com.example.demo.service;

import com.example.demo.dto.EstadisticasRegionDTO;
import com.example.demo.dto.EstadisticasTotalesDTO;
import com.example.demo.model.*;
import com.example.demo.repository.MovimientoRepository;
import com.example.demo.repository.SolicitudRepository;
import com.example.demo.repository.UsuarioAppRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private SolicitudRepository solicitudRepository;
    
    @Autowired
    private UsuarioAppRepository usuarioAppRepository;
    
    @Autowired
    private MovimientoRepository movimientoRepository;

    /**
     * Obtiene estadísticas para la región del usuario autenticado.
     * Si es ADMIN, puede especificar una región o obtener todas.
     */
    public EstadisticasRegionDTO obtenerEstadisticasPorRegion(Region regionEspecifica) {
        return obtenerEstadisticasPorRegionYFecha(regionEspecifica, null, null);
    }

    /**
     * Obtiene estadísticas con filtro de fechas
     */
    public EstadisticasRegionDTO obtenerEstadisticasPorRegionYFecha(Region regionEspecifica, LocalDate fechaDesde, LocalDate fechaHasta) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UsuarioApp usuarioApp = usuarioAppRepository.findByUsername(username);
        
        Region regionParaCalcular;
        
        // Determinar qué región usar
        if (usuarioApp.getRol() == Rol.ADMIN && regionEspecifica != null) {
            regionParaCalcular = regionEspecifica;
        } else {
            regionParaCalcular = usuarioApp.getRegion();
        }
        
        return calcularEstadisticas(regionParaCalcular, fechaDesde, fechaHasta);
    }

    /**
     * Método para que ADMIN obtenga estadísticas de todas las regiones
     */
    public List<EstadisticasRegionDTO> obtenerEstadisticasTodasRegiones() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UsuarioApp usuarioApp = usuarioAppRepository.findByUsername(username);
        
        if (usuarioApp.getRol() != Rol.ADMIN) {
            throw new SecurityException("Solo los administradores pueden ver estadísticas de todas las regiones");
        }
        
        return List.of(Region.values()).stream()
                .map(region -> calcularEstadisticas(region, null, null))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene estadísticas totales del sistema (sin filtros de región)
     */
    public EstadisticasTotalesDTO obtenerEstadisticasTotales() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UsuarioApp usuarioApp = usuarioAppRepository.findByUsername(username);
        
        List<Solicitud> todasSolicitudes;
        List<Movimiento> todosMovimientos;
        
        // Filtrar por región si no es admin
        if (usuarioApp.getRol() == Rol.ADMIN) {
            todasSolicitudes = solicitudRepository.findAll();
            todosMovimientos = movimientoRepository.findAll();
        } else {
            todasSolicitudes = solicitudRepository.findAll().stream()
                .filter(s -> s.getRegion() == usuarioApp.getRegion())
                .collect(Collectors.toList());
            todosMovimientos = movimientoRepository.findAll().stream()
                .filter(m -> m.getUsuario() != null && m.getUsuario().getRegion() == usuarioApp.getRegion())
                .collect(Collectors.toList());
        }
        
        // Calcular totales
        int totalSolicitudes = todasSolicitudes.size();
        int totalMovimientos = todosMovimientos.size();
        
        // Mes actual
        LocalDate ahora = LocalDate.now();
        LocalDate inicioMesActual = ahora.withDayOfMonth(1);
        LocalDate finMesActual = ahora.withDayOfMonth(ahora.lengthOfMonth());
        
        int solicitudesMesActual = (int) todasSolicitudes.stream()
            .filter(s -> s.getFecha() != null)
            .filter(s -> !s.getFecha().isBefore(inicioMesActual) && !s.getFecha().isAfter(finMesActual))
            .count();
        
        int movimientosMesActual = (int) todosMovimientos.stream()
            .filter(m -> m.getFecha() != null)
            .filter(m -> !m.getFecha().isBefore(inicioMesActual) && !m.getFecha().isAfter(finMesActual))
            .count();
        
        // Estadísticas mensuales (últimos 12 meses)
        List<EstadisticasTotalesDTO.EstadisticaMensualDTO> estadisticasMensuales = new ArrayList<>();
        
        for (int i = 11; i >= 0; i--) {
            LocalDate mesCalcular = ahora.minusMonths(i);
            LocalDate inicioMes = mesCalcular.withDayOfMonth(1);
            LocalDate finMes = mesCalcular.withDayOfMonth(mesCalcular.lengthOfMonth());
            
            int solicitudesMes = (int) todasSolicitudes.stream()
                .filter(s -> s.getFecha() != null)
                .filter(s -> !s.getFecha().isBefore(inicioMes) && !s.getFecha().isAfter(finMes))
                .count();
            
            int movimientosMes = (int) todosMovimientos.stream()
                .filter(m -> m.getFecha() != null)
                .filter(m -> !m.getFecha().isBefore(inicioMes) && !m.getFecha().isAfter(finMes))
                .count();
            
            String nombreMes = mesCalcular.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es"));
            
            estadisticasMensuales.add(new EstadisticasTotalesDTO.EstadisticaMensualDTO(
                nombreMes, mesCalcular.getYear(), movimientosMes, solicitudesMes
            ));
        }
        
        return new EstadisticasTotalesDTO(
            totalMovimientos,
            totalSolicitudes,
            movimientosMesActual,
            solicitudesMesActual,
            estadisticasMensuales
        );
    }

    private EstadisticasRegionDTO calcularEstadisticas(Region region, LocalDate fechaDesde, LocalDate fechaHasta) {
        // Obtener usuarios de la región
        List<Usuario> usuariosRegion = usuarioRepository.findAll().stream()
                .filter(u -> u.getRegion() == region)
                .collect(Collectors.toList());
        
        // Obtener solicitudes de la región con filtro de fechas
        List<Solicitud> solicitudesRegion = solicitudRepository.findAll().stream()
                .filter(s -> s.getRegion() == region)
                .filter(s -> filtrarPorFecha(s, fechaDesde, fechaHasta))
                .collect(Collectors.toList());
        
        // Calcular estadísticas
        int totalUsuarios = usuariosRegion.size();
        int totalSolicitudes = solicitudesRegion.size();
        
        // Solicitudes por estado
        Map<EstadoSolicitud, Integer> solicitudesPorEstado = new HashMap<>();
        for (EstadoSolicitud estado : EstadoSolicitud.values()) {
            int count = (int) solicitudesRegion.stream()
                    .filter(s -> s.getEstado() == estado)
                    .count();
            solicitudesPorEstado.put(estado, count);
        }
        
        // Celulares rotos por usuario
        Map<String, Integer> celularesRotosPorUsuario = usuariosRegion.stream()
                .collect(Collectors.toMap(
                    Usuario::getNumReparto,
                    Usuario::getCantCelularesRotos
                ));
        
        // Promedio de celulares rotos
        double promedioCelularesRotos = usuariosRegion.stream()
                .mapToInt(Usuario::getCantCelularesRotos)
                .average()
                .orElse(0.0);
        
        // Solicitudes por mes
        Map<String, Integer> solicitudesPorMes = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        solicitudesRegion.stream()
                .filter(s -> s.getFecha() != null)
                .collect(Collectors.groupingBy(
                    s -> s.getFecha().format(formatter),
                    Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ))
                .forEach(solicitudesPorMes::put);
        
        return new EstadisticasRegionDTO(
                region,
                totalUsuarios,
                totalSolicitudes,
                solicitudesPorEstado,
                celularesRotosPorUsuario,
                promedioCelularesRotos,
                fechaDesde,
                fechaHasta,
                solicitudesPorMes
        );
    }
    
    private boolean filtrarPorFecha(Solicitud solicitud, LocalDate fechaDesde, LocalDate fechaHasta) {
        if (solicitud.getFecha() == null) {
            return false;
        }
        
        LocalDate fechaSolicitud = solicitud.getFecha();
        
        if (fechaDesde != null && fechaSolicitud.isBefore(fechaDesde)) {
            return false;
        }
        
        if (fechaHasta != null && fechaSolicitud.isAfter(fechaHasta)) {
            return false;
        }
        
        return true;
    }
}
