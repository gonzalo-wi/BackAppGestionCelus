
package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Celular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numeroSerie;

    private String codigoInterno;  // Código interno para seguimiento
    private String marca;
    private String modelo;

    @Enumerated(EnumType.STRING)
    private EstadoCelular estado;

    @ManyToOne
    @JsonIgnoreProperties({"zona", "region", "celular", "cantCelularesRotos"})
    private Usuario usuario;

    public Celular() {}

    public Celular(String codigoInterno, String marca, String modelo, Usuario usuario, EstadoCelular estado) {
        this.codigoInterno = codigoInterno;
        this.marca   =   marca;
        this.modelo  =  modelo;
        this.usuario = usuario;
        this.estado  =  estado;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getNumeroSerie() {
        return numeroSerie;
    }

    public EstadoCelular getEstado() {
        return estado;
    }

    public void setEstado(EstadoCelular estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void asignarUsuario(Usuario usuario) {
        this.setUsuario(usuario);
    }
}


