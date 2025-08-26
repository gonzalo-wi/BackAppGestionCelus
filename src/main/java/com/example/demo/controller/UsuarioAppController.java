package com.example.demo.controller;

import com.example.demo.dto.CambioPasswordDTO;
import com.example.demo.dto.UsuarioAppDTO;
import com.example.demo.service.UsuarioAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin/usuarios-sistema")
public class UsuarioAppController {

    @Autowired
    private UsuarioAppService usuarioAppService;

    /**
     * Crear un nuevo usuario del sistema
     */
    @PostMapping
    public ResponseEntity<UsuarioAppDTO> crearUsuario(@RequestBody UsuarioAppDTO usuarioDTO) {
        try {
            UsuarioAppDTO usuarioCreado = usuarioAppService.crearUsuario(usuarioDTO);
            return ResponseEntity.ok(usuarioCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtener todos los usuarios del sistema
     */
    @GetMapping
    public ResponseEntity<List<UsuarioAppDTO>> obtenerTodosLosUsuarios() {
        try {
            List<UsuarioAppDTO> usuarios = usuarioAppService.obtenerTodosLosUsuarios();
            return ResponseEntity.ok(usuarios);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * Actualizar datos de un usuario
     */
    @PutMapping("/{username}")
    public ResponseEntity<UsuarioAppDTO> actualizarUsuario(
            @PathVariable String username, 
            @RequestBody UsuarioAppDTO usuarioDTO) {
        try {
            UsuarioAppDTO usuarioActualizado = usuarioAppService.actualizarUsuario(username, usuarioDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Cambiar contraseña de un usuario
     */
    @PutMapping("/cambiar-password")
    public ResponseEntity<String> cambiarPassword(@RequestBody CambioPasswordDTO cambioDTO) {
        try {
            usuarioAppService.cambiarPassword(cambioDTO);
            return ResponseEntity.ok("Contraseña actualizada exitosamente");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body("No autorizado");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /**
     * Eliminar un usuario del sistema
     */
    @DeleteMapping("/{username}")
    public ResponseEntity<String> eliminarUsuario(@PathVariable String username) {
        try {
            usuarioAppService.eliminarUsuario(username);
            return ResponseEntity.ok("Usuario eliminado exitosamente");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
