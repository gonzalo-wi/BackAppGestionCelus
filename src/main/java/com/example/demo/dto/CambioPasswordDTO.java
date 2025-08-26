package com.example.demo.dto;

public class CambioPasswordDTO {
    private String username;
    private String nuevaPassword;

    public CambioPasswordDTO() {}

    public CambioPasswordDTO(String username, String nuevaPassword) {
        this.username = username;
        this.nuevaPassword = nuevaPassword;
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNuevaPassword() {
        return nuevaPassword;
    }

    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }
}
