
package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Celular {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numeroSerie;

    private String marca;
    private String modelo;

    @Enumerated(EnumType.STRING)
    private EstadoCelular estado;

    @ManyToOne
    @JsonIgnoreProperties({"zona", "region", "celular", "cantCelularesRotos"})
    private Usuario usuario;

    public Celular() {}

    public Celular(String marca, String modelo, Usuario usuario, EstadoCelular estado) {
        this.marca   =   marca;
        this.modelo  =  modelo;
        this.usuario = usuario;
        this.estado  =  estado;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
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


