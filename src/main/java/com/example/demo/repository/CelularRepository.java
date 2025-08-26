package com.example.demo.repository;

import com.example.demo.model.Celular;
import com.example.demo.model.EstadoCelular;
import com.example.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CelularRepository extends JpaRepository<Celular, Long> {
    List<Celular> findByUsuario(Usuario usuario);
    List<Celular> findByUsuarioIsNull();
    List<Celular> findByEstado(EstadoCelular estado);
    List<Celular> findByUsuario_Region(String region);
    Optional<Celular> findByCodigoInterno(String codigoInterno);
}
