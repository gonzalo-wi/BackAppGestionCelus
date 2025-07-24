package com.example.demo.service;

import com.example.demo.model.Movimiento;
import com.example.demo.model.Celular;
import com.example.demo.model.Usuario;
import com.example.demo.repository.MovimientoRepository;
import com.example.demo.repository.CelularRepository;
import com.example.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        Celular celular = celularRepository.findById(movimiento.getCelular().getNumeroSerie())
                .orElseThrow(() -> new RuntimeException("Celular no encontrado"));
        Usuario usuario = usuarioRepository.findById(movimiento.getUsuario().getNumReparto())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Si el usuario ya tiene un celular, desasignarlo
        if (usuario.getCelular() != null) {
            Celular anterior = usuario.getCelular();
            anterior.setUsuario(null);
            celularRepository.save(anterior);
        }

        // Asignar el nuevo celular al usuario
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
}
