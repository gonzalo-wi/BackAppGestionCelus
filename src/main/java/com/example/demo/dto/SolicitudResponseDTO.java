package com.example.demo.dto;

import com.example.demo.model.EstadoSolicitud;
import com.example.demo.model.TipoSolicitud;
import com.example.demo.model.Region;
import java.time.LocalDate;

public class SolicitudResponseDTO {
    private String id;
    private String nomSolicitante;
    private TipoSolicitud tipoSolicitud;
    private String motivo;
    private boolean necesitaLinea;
    private String mailAutorizante;
    private EstadoSolicitud estado;
    private LocalDate fecha;
    private Region region;
    private String usuarioCreador;
    private String usuario;

    // Constructor vacío
    public SolicitudResponseDTO() {}

    // Constructor completo
    public SolicitudResponseDTO(String id, String nomSolicitante, TipoSolicitud tipoSolicitud, 
                               String motivo, boolean necesitaLinea, String mailAutorizante, 
                               EstadoSolicitud estado, LocalDate fecha, Region region, 
                               String usuarioCreador, String usuario) {
        this.id = id;
        this.nomSolicitante = nomSolicitante;
        this.tipoSolicitud = tipoSolicitud;
        this.motivo = motivo;
        this.necesitaLinea = necesitaLinea;
        this.mailAutorizante = mailAutorizante;
        this.estado = estado;
        this.fecha = fecha;
        this.region = region;
        this.usuarioCreador = usuarioCreador;
        this.usuario = usuario;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNomSolicitante() { return nomSolicitante; }
    public void setNomSolicitante(String nomSolicitante) { this.nomSolicitante = nomSolicitante; }

    public TipoSolicitud getTipoSolicitud() { return tipoSolicitud; }
    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) { this.tipoSolicitud = tipoSolicitud; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public boolean isNecesitaLinea() { return necesitaLinea; }
    public void setNecesitaLinea(boolean necesitaLinea) { this.necesitaLinea = necesitaLinea; }

    public String getMailAutorizante() { return mailAutorizante; }
    public void setMailAutorizante(String mailAutorizante) { this.mailAutorizante = mailAutorizante; }

    public EstadoSolicitud getEstado() { return estado; }
    public void setEstado(EstadoSolicitud estado) { this.estado = estado; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Region getRegion() { return region; }
    public void setRegion(Region region) { this.region = region; }

    public String getUsuarioCreador() { return usuarioCreador; }
    public void setUsuarioCreador(String usuarioCreador) { this.usuarioCreador = usuarioCreador; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
}
