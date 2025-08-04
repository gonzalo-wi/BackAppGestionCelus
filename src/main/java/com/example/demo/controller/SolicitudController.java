package com.example.demo.controller;

import com.example.demo.model.Solicitud;
import com.example.demo.model.EstadoSolicitud;
import com.example.demo.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    @Autowired
    private SolicitudService solicitudService;

    @GetMapping
    public List<Solicitud> getAll() {
        return solicitudService.getAll();
    }

    @GetMapping("/usuario/{usuario}")
    public List<Solicitud> getByUsuario(@PathVariable String usuario) {
        return solicitudService.getByUsuario(usuario);
    }

    @PostMapping
    public Solicitud crearSolicitud(@RequestBody Solicitud solicitud) {
        return solicitudService.crearSolicitud(solicitud);
    }

    @PutMapping("/{id}/estado")
    public Solicitud cambiarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
        EstadoSolicitud nuevoEstado = EstadoSolicitud.valueOf(body.get("estado"));
        return solicitudService.cambiarEstado(id, nuevoEstado);
    }
}
