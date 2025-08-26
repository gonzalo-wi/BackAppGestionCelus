package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.MovimientoRepository;
import com.example.demo.repository.CelularRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimientoService {
    @Autowired
    private MovimientoRepository movimientoRepository;
    @Autowired
    private CelularRepository celularRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Movimiento crearMovimiento(Movimiento movimiento) {
        Celular celular = celularRepository.findById((long) movimiento.getCelular().getNumeroSerie())
                .orElseThrow(() -> new RuntimeException("Celular no encontrado"));
        Usuario usuario = usuarioRepository.findById(movimiento.getUsuario().getNumReparto())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        
        if (usuario.getCelular() != null) {
            Celular anterior = usuario.getCelular();
            anterior.setUsuario(null);
            celularRepository.save(anterior);
        }

        
        movimiento.asignarCelular(celular);
        movimiento.asignarUsuario(usuario);
        usuario.asignarCelular(celular);
        celular.asignarUsuario(usuario);
        usuario.incrementarCelularesRotos();
        celularRepository.save(celular);
        usuarioRepository.save(usuario);
        return movimientoRepository.save(movimiento);
    }

    public List<Movimiento> getAll() {
        return movimientoRepository.findAll();
    }

    /**
     * Crear movimiento específico por asignación/entrega
     */
    @Transactional
    public Movimiento crearMovimientoAsignacion(Integer numeroSerie, String numReparto, String descripcion) {
        Celular celular = celularRepository.findById(numeroSerie.longValue())
            .orElseThrow(() -> new RuntimeException("Celular no encontrado"));
        
        Usuario usuario = usuarioRepository.findById(numReparto)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar que el celular esté disponible para asignar
        if (celular.getEstado() != EstadoCelular.NUEVO && 
            celular.getEstado() != EstadoCelular.DISPONIBLE && 
            celular.getEstado() != EstadoCelular.REACONDICIONADO) {
            throw new RuntimeException("El celular no está disponible para asignación");
        }

        // Si el usuario ya tiene un celular, devolverlo primero (movimiento DEVOLUCION)
        if (usuario.getCelular() != null) {
            Celular celularAnterior = usuario.getCelular();
            celularAnterior.setEstado(EstadoCelular.DISPONIBLE);
            celularAnterior.setUsuario(null);
            celularRepository.save(celularAnterior);

            // Registrar devolución del celular anterior
            Movimiento devolucion = new Movimiento(
                LocalDate.now(),
                celularAnterior,
                usuario,
                "Devolución por nueva asignación",
                TipoMovimiento.DEVOLUCION
            );
            movimientoRepository.save(devolucion);
        }

        // Asignar nuevo celular
        celular.setEstado(EstadoCelular.ASIGNADO);
        celular.setUsuario(usuario);
        celularRepository.save(celular);

        // Crear movimiento de entrega
        Movimiento movimientoEntrega = new Movimiento(
            LocalDate.now(),
            celular,
            usuario,
            descripcion,
            TipoMovimiento.ENTREGA
        );

        return movimientoRepository.save(movimientoEntrega);
    }

    /**
     * Actualizar un movimiento existente
     */
    public Movimiento actualizarMovimiento(Long id, Movimiento movimientoActualizado) {
        Movimiento movimientoExistente = movimientoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // Actualizar campos permitidos (no se debe cambiar celular ni usuario después de creado)
        movimientoExistente.setDescripcion(movimientoActualizado.getDescripcion());
        if (movimientoActualizado.getFecha() != null) {
            movimientoExistente.setFecha(movimientoActualizado.getFecha());
        }
        if (movimientoActualizado.getTipo() != null) {
            movimientoExistente.setTipo(movimientoActualizado.getTipo());
        }

        return movimientoRepository.save(movimientoExistente);
    }

    /**
     * Eliminar un movimiento
     */
    @Transactional
    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));

        // Verificar si es seguro eliminar este movimiento
        // Por ejemplo, no eliminar movimientos de asignación que estén activos
        movimientoRepository.delete(movimiento);
    }

    /**
     * Buscar un movimiento por ID
     */
    public Movimiento buscarPorId(Long id) {
        return movimientoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Movimiento no encontrado"));
    }
}
