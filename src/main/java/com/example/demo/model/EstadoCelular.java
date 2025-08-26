package com.example.demo.model;

public enum EstadoCelular {
    NUEVO,              // Celular nuevo, disponible para asignar
    DISPONIBLE,         // Celular usado pero funcional, disponible para asignar
    ASIGNADO,          // Celular actualmente asignado a un usuario
    ROTO,              // Celular reportado como roto, no disponible
    EN_REPARACION,     // Celular enviado a reparar
    REACONDICIONADO,   // Celular reparado, listo para volver a asignar
    DADO_DE_BAJA       // Celular irreparable o muy antiguo
}
