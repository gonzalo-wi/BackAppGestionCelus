package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.CelularRepository;
import com.example.demo.repository.MovimientoRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GestionCelularService {

    @Autowired
    private CelularRepository celularRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MovimientoRepository movimientoRepository;

    /**
     * Reportar celular como roto (usuario queda sin celular hasta asignación manual)
     */
    @Transactional
    public CelularReemplazoResultado reportarCelularRoto(String codigoInterno, String numReparto, String motivoRotura) {
        // Buscar el celular por código interno
        Celular celularRoto = celularRepository.findByCodigoInterno(codigoInterno)
            .orElseThrow(() -> new RuntimeException("Celular no encontrado con código interno: " + codigoInterno));
        
        Usuario usuario = usuarioRepository.findById(numReparto)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el celular esté asignado al usuario
        if (celularRoto.getUsuario() == null || !celularRoto.getUsuario().equals(usuario)) {
            throw new RuntimeException("El celular no está asignado a este usuario");
        }

        // Marcar celular como roto y desasignar
        celularRoto.setEstado(EstadoCelular.ROTO);
        celularRoto.setUsuario(null);
        celularRepository.save(celularRoto);

        // Registrar movimiento de reporte de rotura
        String descripcionRotura = String.format("Celular reportado como roto. Motivo: %s. Usuario queda sin celular asignado.", motivoRotura);
        Movimiento movimientoRotura = new Movimiento(
            LocalDate.now(),
            celularRoto,
            usuario,
            descripcionRotura,
            TipoMovimiento.DEVOLUCION
        );
        movimientoRepository.save(movimientoRotura);

        // Crear resultado sin reemplazo automático
        CelularReemplazoResultado resultado = new CelularReemplazoResultado();
        resultado.setCelularRoto(celularRoto);
        resultado.setUsuario(usuario);
        resultado.setCelularReemplazo(null); // No hay reemplazo automático
        resultado.setExitoReemplazo(false);
        resultado.setMensaje("Celular reportado como roto. Usuario queda sin celular hasta asignación manual.");

        return resultado;
    }    /**
     * Crear movimiento de devolución (celular queda disponible)
     */
    @Transactional
    public void crearDevolucion(Integer numeroSerie, String numReparto, String observaciones) {
        Celular celular = celularRepository.findById(numeroSerie.longValue())
            .orElseThrow(() -> new RuntimeException("Celular no encontrado"));

        if (celular.getUsuario() == null) {
            throw new RuntimeException("El celular no está asignado a ningún usuario");
        }

        Usuario usuario = celular.getUsuario();
        
        // Desasignar y cambiar estado a disponible
        celular.setEstado(EstadoCelular.DISPONIBLE);
        celular.setUsuario(null);
        celularRepository.save(celular);

        // Registrar movimiento
        Movimiento devolucion = new Movimiento(
            LocalDate.now(),
            celular,
            usuario,
            "Devolución: " + observaciones,
            TipoMovimiento.DEVOLUCION
        );
        movimientoRepository.save(devolucion);
    }

    /**
     * Cambiar estado de celular directamente (método simplificado)
     */
    @Transactional
    public void cambiarEstadoCelular(Integer numeroSerie, EstadoCelular nuevoEstado, String observaciones) {
        Celular celular = celularRepository.findById(numeroSerie.longValue())
            .orElseThrow(() -> new RuntimeException("Celular no encontrado"));

        Usuario usuarioAnterior = celular.getUsuario();
        EstadoCelular estadoAnterior = celular.getEstado();

        // Lógica de desasignación automática para estados que no permiten usuario
        if ((nuevoEstado == EstadoCelular.ROTO || 
             nuevoEstado == EstadoCelular.DISPONIBLE || 
             nuevoEstado == EstadoCelular.REACONDICIONADO ||
             nuevoEstado == EstadoCelular.EN_REPARACION ||
             nuevoEstado == EstadoCelular.DADO_DE_BAJA) && 
            celular.getUsuario() != null) {
            
            celular.setUsuario(null); // Desasignar automáticamente
        }

        celular.setEstado(nuevoEstado);
        celularRepository.save(celular);

        // Solo registrar movimiento si había usuario asignado y se desasignó
        if (usuarioAnterior != null && celular.getUsuario() == null) {
            String descripcion = String.format("Cambio de estado: %s → %s. %s", 
                estadoAnterior, nuevoEstado, observaciones != null ? observaciones : "");

            Movimiento movimiento = new Movimiento(
                LocalDate.now(),
                celular,
                usuarioAnterior,
                descripcion,
                TipoMovimiento.DEVOLUCION
            );
            movimientoRepository.save(movimiento);
        }
    }

    /**
     * Obtener celulares por estado
     */
    public List<Celular> obtenerCelularesPorEstado(EstadoCelular estado) {
        return celularRepository.findAll().stream()
            .filter(c -> c.getEstado() == estado)
            .collect(Collectors.toList());
    }

    /**
     * Buscar celular por código interno
     */
    public Optional<Celular> buscarPorCodigoInterno(String codigoInterno) {
        return celularRepository.findByCodigoInterno(codigoInterno);
    }

    // Clase para el resultado del reemplazo
    public static class CelularReemplazoResultado {
        private Celular celularRoto;
        private Celular celularReemplazo;
        private Usuario usuario;
        private boolean exitoReemplazo;
        private String mensaje;

        // Getters y setters
        public Celular getCelularRoto() { return celularRoto; }
        public void setCelularRoto(Celular celularRoto) { this.celularRoto = celularRoto; }
        
        public Celular getCelularReemplazo() { return celularReemplazo; }
        public void setCelularReemplazo(Celular celularReemplazo) { this.celularReemplazo = celularReemplazo; }
        
        public Usuario getUsuario() { return usuario; }
        public void setUsuario(Usuario usuario) { this.usuario = usuario; }
        
        public boolean isExitoReemplazo() { return exitoReemplazo; }
        public void setExitoReemplazo(boolean exitoReemplazo) { this.exitoReemplazo = exitoReemplazo; }
        
        public String getMensaje() { return mensaje; }
        public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    }
}
