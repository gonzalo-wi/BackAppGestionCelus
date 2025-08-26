package com.example.demo.controller;

import com.example.demo.dto.EstadisticasRegionDTO;
import com.example.demo.dto.EstadisticasTotalesDTO;
import com.example.demo.model.Region;
import com.example.demo.service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    /**
     * Obtiene estadísticas de la región del usuario autenticado.
     * Si es ADMIN y especifica una región, obtiene esa región específica.
     */
    @GetMapping("/mi-region")
    public EstadisticasRegionDTO obtenerEstadisticasMiRegion(
            @RequestParam(required = false) Region region,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {
        return estadisticasService.obtenerEstadisticasPorRegionYFecha(region, fechaDesde, fechaHasta);
    }

    /**
     * Solo para ADMIN: obtiene estadísticas de todas las regiones
     */
    @GetMapping("/todas-regiones")
    public List<EstadisticasRegionDTO> obtenerEstadisticasTodasRegiones() {
        return estadisticasService.obtenerEstadisticasTodasRegiones();
    }

    /**
     * Obtiene estadísticas de una región específica (solo ADMIN)
     */
    @GetMapping("/region/{region}")
    public EstadisticasRegionDTO obtenerEstadisticasRegion(
            @PathVariable Region region,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {
        return estadisticasService.obtenerEstadisticasPorRegionYFecha(region, fechaDesde, fechaHasta);
    }

    /**
     * Endpoint específico para reportes mensuales
     */
    @GetMapping("/reportes/mensual")
    public EstadisticasRegionDTO obtenerReporteMensual(
            @RequestParam(required = false) Region region,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate mes) {
        // Calcular primer y último día del mes
        LocalDate primerDia = mes.withDayOfMonth(1);
        LocalDate ultimoDia = mes.withDayOfMonth(mes.lengthOfMonth());
        return estadisticasService.obtenerEstadisticasPorRegionYFecha(region, primerDia, ultimoDia);
    }

    /**
     * Estadísticas totales del sistema con datos mensuales
     */
    @GetMapping("/totales")
    public EstadisticasTotalesDTO obtenerEstadisticasTotales() {
        return estadisticasService.obtenerEstadisticasTotales();
    }
}
