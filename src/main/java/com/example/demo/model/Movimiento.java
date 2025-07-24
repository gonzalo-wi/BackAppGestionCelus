package com.example.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;

    @ManyToOne
    private Celular celular;

    @ManyToOne
    private Usuario usuario;

    private String descripcion;

    public Movimiento() {}

    public Movimiento(LocalDate fecha, Celular celular, Usuario usuario, String descripcion) {
        this.fecha       = fecha;
        this.celular     = celular;
        this.usuario     = usuario;
        this.descripcion = descripcion;
    }

    public Movimiento(LocalDate fecha, Celular celular, Usuario usuario) {
        this(fecha, celular, usuario, null);
    }
    public void asignarCelular(Celular celular) {
        this.setCelular(celular);
    }
    public void asignarUsuario(Usuario usuario) {
        this.setUsuario(usuario);
    }
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Celular getCelular() {
        return celular;
    }

    private void setCelular(Celular celular) {
        this.celular = celular;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    private void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
