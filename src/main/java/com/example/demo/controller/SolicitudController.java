package com.example.demo.controller;

import com.example.demo.dto.SolicitudResponseDTO;
import com.example.demo.model.Solicitud;
import com.example.demo.model.EstadoSolicitud;
import com.example.demo.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {
    @Autowired
    private SolicitudService solicitudService;

    @GetMapping
    public List<SolicitudResponseDTO> getAll() {
        return solicitudService.getAll().stream()
                .map(this::convertirASolicitudResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/mias")
    public List<SolicitudResponseDTO> getMias() {
        return solicitudService.getMiasCreadas().stream()
                .map(this::convertirASolicitudResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/usuario/{usuario}")
    public List<SolicitudResponseDTO> getByUsuario(@PathVariable String usuario) {
        return solicitudService.getByUsuario(usuario).stream()
                .map(this::convertirASolicitudResponseDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public SolicitudResponseDTO crearSolicitud(@RequestBody Solicitud solicitud) {
        Solicitud solicitudCreada = solicitudService.crearSolicitud(solicitud);
        return convertirASolicitudResponseDTO(solicitudCreada);
    }

    @PutMapping("/{id}/estado")
    public SolicitudResponseDTO cambiarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
        EstadoSolicitud nuevoEstado = EstadoSolicitud.valueOf(body.get("estado"));
        Solicitud solicitudActualizada = solicitudService.cambiarEstado(id, nuevoEstado);
        return convertirASolicitudResponseDTO(solicitudActualizada);
    }

    private SolicitudResponseDTO convertirASolicitudResponseDTO(Solicitud solicitud) {
        return new SolicitudResponseDTO(
            solicitud.getId(),
            solicitud.getNomSolicitante(),
            solicitud.getTipoSolicitud(),
            solicitud.getMotivo(),
            solicitud.isNecesitaLinea(),
            solicitud.getMailAutorizante(),
            solicitud.getEstado(),
            solicitud.getFecha(),
            solicitud.getRegion(),
            solicitud.getUsuarioCreador().getUsername(),
            solicitud.getUsuario()
        );
    }
}
