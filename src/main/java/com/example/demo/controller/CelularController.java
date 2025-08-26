package com.example.demo.controller;

import com.example.demo.model.Celular;
import com.example.demo.model.EstadoCelular;
import com.example.demo.model.Usuario;
import com.example.demo.service.CelularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/celulares")
public class CelularController {
    @Autowired
    private CelularService celularService;

    @GetMapping
    public List<Celular> getAll() {
        return celularService.getAll();
    }

    @PostMapping
    public Celular create(@RequestBody Celular celular) {
        return celularService.create(celular);
    }

    @GetMapping("/{numeroSerie}")
    public Celular getByNumeroSerie(@PathVariable int numeroSerie) {
        return celularService.buscarPorNumeroSerie(numeroSerie).orElse(null);
    }

    @PutMapping("/{numeroSerie}")
    public Celular update(@PathVariable int numeroSerie, @RequestBody Celular celular) {
        return celularService.update(numeroSerie, celular);
    }

    @PutMapping("/{numeroSerie}/estado")
    public boolean cambiarEstado(@PathVariable int numeroSerie, @RequestBody Map<String, String> body) {
        EstadoCelular nuevoEstado = EstadoCelular.valueOf(body.get("estado"));
        return celularService.cambiarEstado(numeroSerie, nuevoEstado);
    }

    @PutMapping("/{numeroSerie}/usuario")
    public boolean asignarUsuario(@PathVariable int numeroSerie, @RequestBody Usuario usuario) {
        return celularService.asignarUsuario(numeroSerie, usuario);
    }

    @PutMapping("/{numeroSerie}/codigo-interno")
    public ResponseEntity<Celular> actualizarCodigoInterno(@PathVariable int numeroSerie, @RequestBody Map<String, String> body) {
        try {
            String nuevoCodigoInterno = body.get("codigoInterno");
            if (nuevoCodigoInterno == null || nuevoCodigoInterno.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            Celular celularActualizado = celularService.actualizarCodigoInterno(numeroSerie, nuevoCodigoInterno);
            return ResponseEntity.ok(celularActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
