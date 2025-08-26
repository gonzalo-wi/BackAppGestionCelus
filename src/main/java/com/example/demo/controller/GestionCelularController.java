package com.example.demo.controller;

import com.example.demo.model.Celular;
import com.example.demo.model.EstadoCelular;
import com.example.demo.service.GestionCelularService;
import com.example.demo.service.GestionCelularService.CelularReemplazoResultado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/celulares/gestion")
public class GestionCelularController {

    @Autowired
    private GestionCelularService gestionCelularService;

    /**
     * Reportar celular como roto (usuario queda sin celular hasta asignación manual)
     */
    @PostMapping("/reportar-roto")
    public ResponseEntity<CelularReemplazoResultado> reportarCelularRoto(
            @RequestBody Map<String, String> request) {
        try {
            String codigoInterno = request.get("codigoInterno");
            String numReparto = request.get("numReparto");
            String motivoRotura = request.get("motivoRotura");

            CelularReemplazoResultado resultado = gestionCelularService.reportarCelularRoto(
                codigoInterno, numReparto, motivoRotura);
            
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Crear devolución (celular queda disponible)
     */
    @PostMapping("/devolucion/{numeroSerie}")
    public ResponseEntity<String> crearDevolucion(
            @PathVariable Integer numeroSerie,
            @RequestBody Map<String, String> request) {
        try {
            String observaciones = request.get("observaciones");
            String numReparto = request.get("numReparto");
            gestionCelularService.crearDevolucion(numeroSerie, numReparto, observaciones);
            return ResponseEntity.ok("Devolución registrada exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Cambiar estado de celular con lógica automática
     */
    @PostMapping("/cambiar-estado/{numeroSerie}")
    public ResponseEntity<String> cambiarEstado(
            @PathVariable Integer numeroSerie,
            @RequestBody Map<String, String> request) {
        try {
            EstadoCelular nuevoEstado = EstadoCelular.valueOf(request.get("nuevoEstado"));
            String observaciones = request.get("observaciones");
            
            gestionCelularService.cambiarEstadoCelular(numeroSerie, nuevoEstado, observaciones);
            return ResponseEntity.ok("Estado del celular actualizado exitosamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Obtener celulares por estado
     */
    @GetMapping("/por-estado/{estado}")
    public ResponseEntity<List<Celular>> obtenerCelularesPorEstado(@PathVariable EstadoCelular estado) {
        try {
            List<Celular> celulares = gestionCelularService.obtenerCelularesPorEstado(estado);
            return ResponseEntity.ok(celulares);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtener resumen de estados de celulares
     */
    @GetMapping("/resumen-estados")
    public ResponseEntity<Map<EstadoCelular, Long>> obtenerResumenEstados() {
        try {
            Map<EstadoCelular, Long> resumen = java.util.Arrays.stream(EstadoCelular.values())
                .collect(java.util.stream.Collectors.toMap(
                    estado -> estado,
                    estado -> (long) gestionCelularService.obtenerCelularesPorEstado(estado).size()
                ));
            return ResponseEntity.ok(resumen);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Buscar celular por código interno
     */
    @GetMapping("/buscar-codigo/{codigoInterno}")
    public ResponseEntity<Celular> buscarPorCodigoInterno(@PathVariable String codigoInterno) {
        try {
            Optional<Celular> celular = gestionCelularService.buscarPorCodigoInterno(codigoInterno);
            if (celular.isPresent()) {
                return ResponseEntity.ok(celular.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtener celulares disponibles para asignación
     */
    @GetMapping("/disponibles")
    public ResponseEntity<List<Celular>> obtenerCelularesDisponibles() {
        try {
            List<Celular> disponibles = gestionCelularService.obtenerCelularesPorEstado(EstadoCelular.NUEVO);
            disponibles.addAll(gestionCelularService.obtenerCelularesPorEstado(EstadoCelular.DISPONIBLE));
            disponibles.addAll(gestionCelularService.obtenerCelularesPorEstado(EstadoCelular.REACONDICIONADO));
            return ResponseEntity.ok(disponibles);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
