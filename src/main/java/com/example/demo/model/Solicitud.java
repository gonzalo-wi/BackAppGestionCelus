package com.example.demo.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
public class Solicitud {
    private static final EstadoSolicitud ESTADO_INICIAL        = EstadoSolicitud.PENDIENTE;
    private static final String          MAIL_SOPORTE_SISTEMAS = "soporte@el-jumillano.com.ar";

    @Id
    private String id; 
    private String nomSolicitante;
    private LocalDate fecha;
    private String usuario;            
    @Enumerated(EnumType.STRING)
    private Region region;
    @Enumerated(EnumType.STRING)
    private TipoSolicitud tipoSolicitud;
    private String motivo;
    private boolean necesitaLinea;
    @Enumerated(EnumType.STRING)
    private EstadoSolicitud estado;
    private String mailAutorizante;
    
    // Relación con el usuario de la app que creó la solicitud
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_creador")
    @JsonIgnoreProperties({"misSolicitudes", "flota"})
    private UsuarioApp usuarioCreador;
   

    
    public Solicitud() {
        this.estado = ESTADO_INICIAL;
    }

    
    public Solicitud(String id, String nomSolicitante, LocalDate fecha, String usuario, Region region,
                     TipoSolicitud tipoSolicitud, String motivo, boolean necesitaLinea, String mailAutorizante) {
        this.id              = id;
        this.nomSolicitante  = nomSolicitante;
        this.fecha           = fecha;
        this.usuario         = usuario;
        this.region          = region;
        this.tipoSolicitud   = tipoSolicitud;
        this.motivo          = motivo;
        this.necesitaLinea   = necesitaLinea;
        this.estado          = ESTADO_INICIAL;
        this.mailAutorizante = mailAutorizante;
    }

    public void cambiarEstado(EstadoSolicitud nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public boolean necesitaAutorizante(){
       return this.tipoSolicitud == TipoSolicitud.NUEVO_EQUIPO; 
    }

    public String obtenerMailSoporte(){
        return MAIL_SOPORTE_SISTEMAS;
    }

   

    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNomSolicitante() { return nomSolicitante; }
    public void setNomSolicitante(String nomSolicitante) { this.nomSolicitante = nomSolicitante; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }
    public TipoSolicitud getTipoSolicitud() { return tipoSolicitud; }
    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) { this.tipoSolicitud = tipoSolicitud; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public boolean isNecesitaLinea() { return necesitaLinea; }
    public void setNecesitaLinea(boolean necesitaLinea) { this.necesitaLinea = necesitaLinea; }
    public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }
    public String getMailAutorizante() { return mailAutorizante; }

    public UsuarioApp getUsuarioCreador() { return usuarioCreador; }
    public void setUsuarioCreador(UsuarioApp usuarioCreador) { this.usuarioCreador = usuarioCreador; }
}
