package com.example.demo.service;

import com.example.demo.model.Solicitud;
import com.example.demo.repository.SolicitudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SolicitudService {
    @Autowired
    private SolicitudRepository solicitudRepository;

    public Solicitud crearSolicitud(Solicitud solicitud) {
        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> getAll() {
        return solicitudRepository.findAll();
    }

    public Solicitud cambiarEstado(String id, com.example.demo.model.EstadoSolicitud nuevoEstado) {
        Solicitud solicitud = solicitudRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.cambiarEstado(nuevoEstado);
        return solicitudRepository.save(solicitud);
    }

    public List<Solicitud> getByUsuario(String usuario) {
        return solicitudRepository.findAll().stream()
            .filter(s -> usuario.equals(s.getUsuario()))
            .toList();
    }
}
