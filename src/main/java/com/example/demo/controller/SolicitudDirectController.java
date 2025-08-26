package com.example.demo.controller;

import com.example.demo.dto.SolicitudResponseDTO;
import com.example.demo.model.Solicitud;
import com.example.demo.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/solicitudes")
public class SolicitudDirectController {
    
    @Autowired
    private SolicitudService solicitudService;

    @GetMapping("/mias")
    public List<SolicitudResponseDTO> getMiasSolicitudes() {
        List<Solicitud> solicitudes = solicitudService.getMiasCreadas();
        return solicitudes.stream()
            .map(this::convertirASolicitudResponseDTO)
            .collect(Collectors.toList());
    }

    @PostMapping("/crear")
    public SolicitudResponseDTO crearSolicitud(@RequestBody Solicitud solicitud) {
        Solicitud solicitudCreada = solicitudService.crearSolicitud(solicitud);
        return convertirASolicitudResponseDTO(solicitudCreada);
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
            solicitud.getUsuarioCreador() != null ? solicitud.getUsuarioCreador().getUsername() : null,
            solicitud.getUsuario()
        );
    }
}
