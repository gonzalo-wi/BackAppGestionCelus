package com.example.demo.model;

import jakarta.persistence.*;


@Entity
public class Usuario {
    private static final int INICIALIZAR_CANTIDAD_CELULARES_ROTOS = 0;
    @Id
    private String numReparto;
    @Enumerated(EnumType.STRING)
    private Zona zona;

    @Enumerated(EnumType.STRING)
    private Region region;

    @OneToOne(mappedBy = "usuario")
    private Celular celular;

    private int cantCelularesRotos;

    public Usuario() {}

    public Usuario(String numReparto, Zona zona, Region region, Celular celular) {
        this.numReparto         = numReparto;
        this.zona               = zona;
        this.region             = region;
        this.celular            = celular;
        this.cantCelularesRotos = INICIALIZAR_CANTIDAD_CELULARES_ROTOS;
    }

    public void asignarCelular(Celular celular) {
        this.setCelular(celular);
    }

    public String getNumReparto() {
        return numReparto;
    }

    public Zona getZona() {
        return zona;
    }

    public Region getRegion() {
        return region;
    }

    public Celular getCelular() {
        return celular;
    }

    public void setCelular(Celular celular) {
        this.celular = celular;
    }

    public int getCantCelularesRotos() {
        return cantCelularesRotos;
    }

    public void incrementarCelularesRotos() {
        this.cantCelularesRotos++;
    }
}
