package com.example.demo.controller;

import com.example.demo.model.Movimiento;
import com.example.demo.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/movimientos")
public class MovimientoController {
    @Autowired
    private MovimientoService movimientoService;

    @GetMapping
    public List<Movimiento> getAll() {
        return movimientoService.getAll();
    }

    @PostMapping
    public Movimiento create(@RequestBody Movimiento movimiento) {
        return movimientoService.crearMovimiento(movimiento);
    }

    @PostMapping("/asignacion")
    public Movimiento crearAsignacion(@RequestBody Map<String, Object> request) {
        Integer numeroSerie = Integer.valueOf(request.get("numeroSerie").toString());
        String numReparto = request.get("numReparto").toString();
        String descripcion = request.get("descripcion") != null ? 
            request.get("descripcion").toString() : "Asignación de celular";
        
        return movimientoService.crearMovimientoAsignacion(numeroSerie, numReparto, descripcion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimiento> getById(@PathVariable Long id) {
        try {
            Movimiento movimiento = movimientoService.buscarPorId(id);
            return ResponseEntity.ok(movimiento);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movimiento> update(@PathVariable Long id, @RequestBody Movimiento movimiento) {
        try {
            Movimiento movimientoActualizado = movimientoService.actualizarMovimiento(id, movimiento);
            return ResponseEntity.ok(movimientoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            movimientoService.eliminarMovimiento(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
