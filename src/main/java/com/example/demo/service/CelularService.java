
package com.example.demo.service;
import com.example.demo.model.Usuario;
import com.example.demo.model.Celular;
import com.example.demo.model.EstadoCelular;
import com.example.demo.repository.CelularRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CelularService {
    @Autowired
    private CelularRepository celularRepository;

    public List<Celular> getAll() {
        return celularRepository.findAll();
    }

    public Celular create(Celular celular) {
        return celularRepository.save(celular);
    }

    public Celular update(int numeroSerie, Celular celularActualizado) {
        Optional<Celular> celularOpt = buscarPorNumeroSerie(numeroSerie);
        if (celularOpt.isPresent()) {
            Celular celularExistente = celularOpt.get();
            
            celularExistente.setMarca(celularActualizado.getMarca());
            celularExistente.setModelo(celularActualizado.getModelo());
            celularExistente.setEstado(celularActualizado.getEstado());
            
            // Actualizar código interno si se proporciona
            if (celularActualizado.getCodigoInterno() != null && 
                !celularActualizado.getCodigoInterno().trim().isEmpty()) {
                celularExistente.setCodigoInterno(celularActualizado.getCodigoInterno());
            }
            
            if (celularActualizado.getUsuario() != null) {
                celularExistente.asignarUsuario(celularActualizado.getUsuario());
            }
            return celularRepository.save(celularExistente);
        } else {
            throw new RuntimeException("Celular con número de serie " + numeroSerie + " no encontrado");
        }
    }

    public Optional<Celular> buscarPorNumeroSerie(int numeroSerie) {
        return celularRepository.findById((long) numeroSerie);
    }

    public boolean cambiarEstado(int numeroSerie, EstadoCelular nuevoEstado) {
        boolean sePudoCambiar = false;
        Optional<Celular> celularOpt = buscarPorNumeroSerie(numeroSerie);
        if (celularOpt.isPresent()) {
            Celular celular = celularOpt.get();
            celular.setEstado(nuevoEstado);
            celularRepository.save(celular);
            sePudoCambiar = true;
        }
        return sePudoCambiar;
    }

    public boolean asignarUsuario(int numeroSerie, Usuario usuario) {
        boolean sePudoAsignar = false;
        Optional<Celular> celularOpt = buscarPorNumeroSerie(numeroSerie);
        if (celularOpt.isPresent()) {
            Celular celular = celularOpt.get();
            celular.asignarUsuario(usuario);
            celularRepository.save(celular);
            sePudoAsignar = true;
        }
        return sePudoAsignar;
    }

    public Celular actualizarCodigoInterno(int numeroSerie, String nuevoCodigoInterno) {
        Optional<Celular> celularOpt = buscarPorNumeroSerie(numeroSerie);
        if (celularOpt.isPresent()) {
            Celular celular = celularOpt.get();
            celular.setCodigoInterno(nuevoCodigoInterno);
            return celularRepository.save(celular);
        } else {
            throw new RuntimeException("Celular con número de serie " + numeroSerie + " no encontrado");
        }
    }
}
