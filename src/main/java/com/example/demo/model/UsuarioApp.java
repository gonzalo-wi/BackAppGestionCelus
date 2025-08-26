package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
public class UsuarioApp {
    private static final int MIN_CARACTERES_PASSWORD = 8;

    @Id
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    @Enumerated(EnumType.STRING)
    private Region region;
    @OneToMany(mappedBy = "usuarioCreador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Solicitud> misSolicitudes;
    // Nota: flota se maneja via repositorio, no relación JPA directa

public UsuarioApp(String username, String password, Rol rol, Region region) {
        this.username = username;
        this.password = password;
        this.rol      = rol;
        this.region   = region;
    }

    public UsuarioApp() {}


     public List<Solicitud> obtenerSolicitudesPorRegion() {
        List<Solicitud> solicitudesDevolver;
        if (this.esAdmin()) {
            solicitudesDevolver = this.misSolicitudes; 
        } else {
             solicitudesDevolver =  misSolicitudes.stream()
                .filter(s -> s.getRegion() == this.region)
                .toList();
        }
        return solicitudesDevolver;
    }

    public List<Usuario> obtenerFlotaDeUsuarios() {
        // Esta lógica se movió al controlador que tiene acceso al repositorio
        throw new UnsupportedOperationException("Usar UsuarioController.getMiFlota() en su lugar");
    }
    private boolean esAdmin() {
        return this.rol == Rol.ADMIN;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

   

    public Rol getRol() {
        return rol;
    }


    public Region getRegion() {
        return region;
    }
    public List<Solicitud> getSolicitudesCreadas() {
        return this.misSolicitudes;
    }

    public List<Usuario> getFlota() {
        // Esta lógica se movió al controlador que tiene acceso al repositorio
        throw new UnsupportedOperationException("Usar UsuarioController.getMiFlota() en su lugar");
    }

    public void cambiarUsername(String newUsername) {
        if(newUsername.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío.");
        }
        this.username = newUsername;
    }

     public void cambiarPassword(String newPassword) {
        if(newPassword.length() < MIN_CARACTERES_PASSWORD || newPassword.isBlank() ) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        this.password = newPassword;
    }

    public void cambiarRol(Rol newRol) {
        this.rol = newRol;
    }

    public void cambiarRegion(Region newRegion) {
        this.region = newRegion;
    }


}