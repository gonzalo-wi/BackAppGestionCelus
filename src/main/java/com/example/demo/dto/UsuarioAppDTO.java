package com.example.demo.dto;

import com.example.demo.model.Region;
import com.example.demo.model.Rol;

public class UsuarioAppDTO {
    private String username;
    private String password;
    private Rol rol;
    private Region region;

    public UsuarioAppDTO() {}

    public UsuarioAppDTO(String username, String password, Rol rol, Region region) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.region = region;
    }

    // Constructor sin password para respuestas
    public UsuarioAppDTO(String username, Rol rol, Region region) {
        this.username = username;
        this.rol = rol;
        this.region = region;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
