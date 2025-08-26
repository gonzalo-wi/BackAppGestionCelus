package com.example.demo.dto;

import com.example.demo.model.EstadoSolicitud;
import com.example.demo.model.Region;

import java.time.LocalDate;
import java.util.Map;

public class EstadisticasRegionDTO {
    private Region                        region;
    private int                           totalUsuarios;
    private int                           totalSolicitudes;
    private Map<EstadoSolicitud, Integer> solicitudesPorEstado;
    private Map<String, Integer>          celularesRotosPorUsuario;
    private double                        promedioCelularesRotos;
    private LocalDate                     fechaDesde;
    private LocalDate                     fechaHasta;
    private Map<String, Integer>          solicitudesPorMes; // "2025-08" -> cantidad

    public EstadisticasRegionDTO() {}

    public EstadisticasRegionDTO(Region region, int totalUsuarios, int totalSolicitudes, 
                                Map<EstadoSolicitud, Integer> solicitudesPorEstado,
                                Map<String, Integer> celularesRotosPorUsuario,
                                double promedioCelularesRotos,
                                LocalDate fechaDesde,
                                LocalDate fechaHasta,
                                Map<String, Integer> solicitudesPorMes) {
        this.region                   = region;
        this.totalUsuarios            = totalUsuarios;
        this.totalSolicitudes         = totalSolicitudes;
        this.solicitudesPorEstado     = solicitudesPorEstado;
        this.celularesRotosPorUsuario = celularesRotosPorUsuario;
        this.promedioCelularesRotos   = promedioCelularesRotos;
        this.fechaDesde               = fechaDesde;
        this.fechaHasta               = fechaHasta;
        this.solicitudesPorMes        = solicitudesPorMes;
    }

    // Getters y Setters
    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public int getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(int totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public int getTotalSolicitudes() {
        return totalSolicitudes;
    }

    public void setTotalSolicitudes(int totalSolicitudes) {
        this.totalSolicitudes = totalSolicitudes;
    }

    public Map<EstadoSolicitud, Integer> getSolicitudesPorEstado() {
        return solicitudesPorEstado;
    }

    public void setSolicitudesPorEstado(Map<EstadoSolicitud, Integer> solicitudesPorEstado) {
        this.solicitudesPorEstado = solicitudesPorEstado;
    }

    public Map<String, Integer> getCelularesRotosPorUsuario() {
        return celularesRotosPorUsuario;
    }

    public void setCelularesRotosPorUsuario(Map<String, Integer> celularesRotosPorUsuario) {
        this.celularesRotosPorUsuario = celularesRotosPorUsuario;
    }

    public double getPromedioCelularesRotos() {
        return promedioCelularesRotos;
    }

    public void setPromedioCelularesRotos(double promedioCelularesRotos) {
        this.promedioCelularesRotos = promedioCelularesRotos;
    }

    public LocalDate getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(LocalDate fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public LocalDate getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(LocalDate fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Map<String, Integer> getSolicitudesPorMes() {
        return solicitudesPorMes;
    }

    public void setSolicitudesPorMes(Map<String, Integer> solicitudesPorMes) {
        this.solicitudesPorMes = solicitudesPorMes;
    }
}
