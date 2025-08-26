package com.example.demo.dto;

import java.util.List;

public class EstadisticasTotalesDTO {
    private int totalMovimientos;
    private int totalSolicitudes;
    private int movimientosMesActual;
    private int solicitudesMesActual;
    private List<EstadisticaMensualDTO> estadisticasMensuales;

    public EstadisticasTotalesDTO() {}

    public EstadisticasTotalesDTO(int totalMovimientos, int totalSolicitudes, 
                                 int movimientosMesActual, int solicitudesMesActual,
                                 List<EstadisticaMensualDTO> estadisticasMensuales) {
        this.totalMovimientos = totalMovimientos;
        this.totalSolicitudes = totalSolicitudes;
        this.movimientosMesActual = movimientosMesActual;
        this.solicitudesMesActual = solicitudesMesActual;
        this.estadisticasMensuales = estadisticasMensuales;
    }

    // Getters y Setters
    public int getTotalMovimientos() {
        return totalMovimientos;
    }

    public void setTotalMovimientos(int totalMovimientos) {
        this.totalMovimientos = totalMovimientos;
    }

    public int getTotalSolicitudes() {
        return totalSolicitudes;
    }

    public void setTotalSolicitudes(int totalSolicitudes) {
        this.totalSolicitudes = totalSolicitudes;
    }

    public int getMovimientosMesActual() {
        return movimientosMesActual;
    }

    public void setMovimientosMesActual(int movimientosMesActual) {
        this.movimientosMesActual = movimientosMesActual;
    }

    public int getSolicitudesMesActual() {
        return solicitudesMesActual;
    }

    public void setSolicitudesMesActual(int solicitudesMesActual) {
        this.solicitudesMesActual = solicitudesMesActual;
    }

    public List<EstadisticaMensualDTO> getEstadisticasMensuales() {
        return estadisticasMensuales;
    }

    public void setEstadisticasMensuales(List<EstadisticaMensualDTO> estadisticasMensuales) {
        this.estadisticasMensuales = estadisticasMensuales;
    }

    // Clase interna para estadísticas mensuales
    public static class EstadisticaMensualDTO {
        private String mes;
        private int year;
        private int movimientos;
        private int solicitudes;

        public EstadisticaMensualDTO() {}

        public EstadisticaMensualDTO(String mes, int year, int movimientos, int solicitudes) {
            this.mes = mes;
            this.year = year;
            this.movimientos = movimientos;
            this.solicitudes = solicitudes;
        }

        // Getters y Setters
        public String getMes() {
            return mes;
        }

        public void setMes(String mes) {
            this.mes = mes;
        }

        public int getYear() {
            return year;
        }

        public void setYear(int year) {
            this.year = year;
        }

        public int getMovimientos() {
            return movimientos;
        }

        public void setMovimientos(int movimientos) {
            this.movimientos = movimientos;
        }

        public int getSolicitudes() {
            return solicitudes;
        }

        public void setSolicitudes(int solicitudes) {
            this.solicitudes = solicitudes;
        }
    }
}
