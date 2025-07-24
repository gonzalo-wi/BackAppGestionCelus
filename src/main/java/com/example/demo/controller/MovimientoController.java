package com.example.demo.controller;

import com.example.demo.model.Movimiento;
import com.example.demo.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

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
}
