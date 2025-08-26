package com.example.demo.repository;

import com.example.demo.model.UsuarioApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioAppRepository extends JpaRepository<UsuarioApp, String> {
    UsuarioApp findByUsername(String username);
}