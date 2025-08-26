package com.example.demo.controller;

import com.example.demo.model.Rol;
import com.example.demo.model.Usuario;
import com.example.demo.model.UsuarioApp;
import com.example.demo.model.Solicitud;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.SolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/usuarios")

public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private com.example.demo.repository.UsuarioAppRepository usuarioAppRepository;
    @Autowired
    private SolicitudService solicitudService;

    @GetMapping
    public List<Usuario> getAll() {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UsuarioApp usuarioApp = usuarioAppRepository.findByUsername(username);
        return usuarioApp.getRol() == Rol.ADMIN
            ? usuarioRepository.findAll()
            : usuarioRepository.findAll().stream()
                .filter(u -> u.getRegion() == usuarioApp.getRegion())
                .toList();
    }

    @PostMapping
    public Usuario create(@RequestBody Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario update(@PathVariable String id, @RequestBody Usuario usuario) {
        // Verificar que el usuario existe
        Usuario usuarioExistente = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        
        // Actualizar los campos
        usuarioExistente.setNumReparto(usuario.getNumReparto());
        usuarioExistente.setZona(usuario.getZona());
        usuarioExistente.setRegion(usuario.getRegion());
        usuarioExistente.setNumeroLinea(usuario.getNumeroLinea());
        usuarioExistente.setCantCelularesRotos(usuario.getCantCelularesRotos());
        
        return usuarioRepository.save(usuarioExistente);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        usuarioRepository.deleteById(id);
    }

    // Flota (usuarios) visibles para el usuario autenticado
    @GetMapping("/mi-flota")
    public List<Usuario> getMiFlota() {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UsuarioApp usuarioApp = usuarioAppRepository.findByUsername(username);
        
        // Implementar filtrado por región/rol aquí directamente
        if (usuarioApp.getRol() == Rol.ADMIN) {
            return usuarioRepository.findAll(); // Admin ve todos los usuarios
        } else {
            // Usuario normal solo ve usuarios de su región
            return usuarioRepository.findAll().stream()
                .filter(u -> u.getRegion() == usuarioApp.getRegion())
                .toList();
        }
    }

    // Mis solicitudes (todas las solicitudes de mi región)
    @GetMapping("/me/solicitudes")
    public List<Solicitud> getMisSolicitudes() {
        return solicitudService.getAll(); // Usa el método que ya filtra por región
    }

    // Actualizar línea de un usuario de la flota
    @PutMapping("/flota/{numReparto}/linea")
    public Usuario actualizarLineaFlota(@PathVariable String numReparto, @RequestBody Map<String, String> body) {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        UsuarioApp usuarioApp = usuarioAppRepository.findByUsername(username);
        
        // Buscar el usuario a actualizar
        Usuario usuario = usuarioRepository.findById(numReparto)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + numReparto));
        
        // Verificar que el usuario pertenece a la misma región (solo si no es admin)
        if (usuarioApp.getRol() != Rol.ADMIN && usuario.getRegion() != usuarioApp.getRegion()) {
            throw new RuntimeException("No tienes permisos para editar usuarios de otra región");
        }
        
        // Actualizar la línea
        String nuevaLinea = body.get("numeroLinea");
        usuario.setNumeroLinea(nuevaLinea);
        
        return usuarioRepository.save(usuario);
    }
}
