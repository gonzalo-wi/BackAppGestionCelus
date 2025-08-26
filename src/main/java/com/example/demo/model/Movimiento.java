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

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    public Movimiento() {}

    public Movimiento(LocalDate fecha, Celular celular, Usuario usuario, String descripcion, TipoMovimiento tipo) {
        this.fecha       = fecha;
        this.celular     = celular;
        this.usuario     = usuario;
        this.descripcion = descripcion;
        this.tipo        = tipo;
    }

    public Movimiento(LocalDate fecha, Celular celular, Usuario usuario) {
        this(fecha, celular, usuario, null, TipoMovimiento.ENTREGA); 
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

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
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

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
