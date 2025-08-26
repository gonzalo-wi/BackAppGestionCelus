package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Usuario {
    private static final int INICIALIZAR_CANTIDAD_CELULARES_ROTOS = 0;
    @Id
    private String numReparto;
    @Enumerated(EnumType.STRING)
    private Zona    zona;

    @Enumerated(EnumType.STRING)
    private Region  region;

    @OneToOne(mappedBy = "usuario")
    @JsonIgnoreProperties("usuario")
    private Celular celular;
    private String  numeroLinea;

    private int     cantCelularesRotos;

    public Usuario() {}

    public Usuario(String numReparto, Zona zona, Region region,String numeroLinea, Celular celular) {
        this.numReparto         = numReparto;
        this.zona               = zona;
        this.region             = region;
        this.numeroLinea        = numeroLinea;
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

    private void setCelular(Celular celular) {
        this.celular = celular;
    }
    public String devolverNumLinea() {
        return this.numeroLinea;
    }

    public String getNumeroLinea() {
        return this.numeroLinea;
    }

    public void setNumeroLinea(String numeroLinea) {
        this.numeroLinea = numeroLinea;
    }

    public void setNumReparto(String numReparto) {
        this.numReparto = numReparto;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setCantCelularesRotos(int cantCelularesRotos) {
        this.cantCelularesRotos = cantCelularesRotos;
    }

    public int getCantCelularesRotos() {
        return cantCelularesRotos;
    }

    public void incrementarCelularesRotos() {
        this.cantCelularesRotos++;
    }
}
