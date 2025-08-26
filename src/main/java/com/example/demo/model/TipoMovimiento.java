package com.example.demo.model;

public enum TipoMovimiento {
    ENTREGA,           // Entrega inicial de celular a usuario
    ASIGNACION,        // Asignación/reasignación de celular
    DEVOLUCION,        // Usuario devuelve celular (por rotura, cambio, etc.)
    REPORTE_ROTURA,    // Movimiento específico por rotura reportada
    REPARACION,        // Movimientos relacionados con reparación
    REEMPLAZO,         // Entrega de celular de reemplazo
    BAJA,              // Dar de baja celular
    MANTENIMIENTO      // Movimientos administrativos/mantenimiento
}
