package com.example.demo.model;

import java.time.LocalDate;

import jakarta.persistence.*;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Solicitud {
    private static final EstadoSolicitud ESTADO_INICIAL = EstadoSolicitud.PENDIENTE;

    @Id
    private String          id;
    private String          nomSolicitante;
    private LocalDate       fecha;
    private String          usuario;
    @Enumerated(EnumType.STRING)
    private Region          region;
    @Enumerated(EnumType.STRING)
    private TipoSolicitud   tipoSolicitud;
    private String          motivo;
    private boolean         necesitaLinea;
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;

    public Solicitud(String id, String nomSolicitante, LocalDate fecha, String usuario, Region region, String motivo, boolean necesitaLinea) {
        this.id             = id;
        this.nomSolicitante = nomSolicitante;
        this.fecha          = fecha;
        this.usuario        = usuario;
        this.region         = region;
        this.motivo         = motivo;
        this.necesitaLinea  = necesitaLinea;
        this.cambiarEstado(ESTADO_INICIAL);
    }
       
    
    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public String getUsuario() {
        return usuario;
    }

 
}
