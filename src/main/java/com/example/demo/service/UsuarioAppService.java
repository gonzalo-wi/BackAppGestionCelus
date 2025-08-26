package com.example.demo.service;

import com.example.demo.dto.CambioPasswordDTO;
import com.example.demo.dto.UsuarioAppDTO;
import com.example.demo.model.Rol;
import com.example.demo.model.UsuarioApp;
import com.example.demo.repository.UsuarioAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioAppService {

    @Autowired
    private UsuarioAppRepository usuarioAppRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Crear un nuevo usuario del sistema (solo ADMIN)
     */
    public UsuarioAppDTO crearUsuario(UsuarioAppDTO usuarioDTO) {
        verificarPermisoAdmin();
        
        // Verificar que el username no exista
        if (usuarioAppRepository.findByUsername(usuarioDTO.getUsername()) != null) {
            throw new RuntimeException("El usuario ya existe: " + usuarioDTO.getUsername());
        }

        // Crear nuevo usuario con password encriptada
        UsuarioApp nuevoUsuario = new UsuarioApp(
            usuarioDTO.getUsername(),
            passwordEncoder.encode(usuarioDTO.getPassword()),
            usuarioDTO.getRol(),
            usuarioDTO.getRegion()
        );

        UsuarioApp usuarioGuardado = usuarioAppRepository.save(nuevoUsuario);
        
        // Retornar DTO sin password
        return new UsuarioAppDTO(
            usuarioGuardado.getUsername(),
            usuarioGuardado.getRol(),
            usuarioGuardado.getRegion()
        );
    }

    /**
     * Obtener todos los usuarios del sistema (solo ADMIN)
     */
    public List<UsuarioAppDTO> obtenerTodosLosUsuarios() {
        verificarPermisoAdmin();
        
        return usuarioAppRepository.findAll().stream()
            .map(usuario -> new UsuarioAppDTO(
                usuario.getUsername(),
                usuario.getRol(),
                usuario.getRegion()
            ))
            .collect(Collectors.toList());
    }

    /**
     * Cambiar contraseña de un usuario (solo ADMIN)
     */
    public void cambiarPassword(CambioPasswordDTO cambioDTO) {
        verificarPermisoAdmin();
        
        UsuarioApp usuario = usuarioAppRepository.findByUsername(cambioDTO.getUsername());
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado: " + cambioDTO.getUsername());
        }

        // Usar el método del dominio que valida la contraseña
        usuario.cambiarPassword(passwordEncoder.encode(cambioDTO.getNuevaPassword()));
        usuarioAppRepository.save(usuario);
    }

    /**
     * Actualizar datos de un usuario (solo ADMIN)
     */
    public UsuarioAppDTO actualizarUsuario(String username, UsuarioAppDTO usuarioDTO) {
        verificarPermisoAdmin();
        
        UsuarioApp usuario = usuarioAppRepository.findByUsername(username);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }

        // Actualizar solo los campos permitidos (no password ni username)
        usuario.cambiarRol(usuarioDTO.getRol());
        usuario.cambiarRegion(usuarioDTO.getRegion());
        
        UsuarioApp usuarioActualizado = usuarioAppRepository.save(usuario);
        
        return new UsuarioAppDTO(
            usuarioActualizado.getUsername(),
            usuarioActualizado.getRol(),
            usuarioActualizado.getRegion()
        );
    }

    /**
     * Eliminar usuario (solo ADMIN)
     */
    public void eliminarUsuario(String username) {
        verificarPermisoAdmin();
        
        UsuarioApp usuario = usuarioAppRepository.findByUsername(username);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado: " + username);
        }

        usuarioAppRepository.delete(usuario);
    }

    private void verificarPermisoAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        UsuarioApp currentUser = usuarioAppRepository.findByUsername(currentUsername);
        
        if (currentUser == null || currentUser.getRol() != Rol.ADMIN) {
            throw new SecurityException("Solo los administradores pueden realizar esta operación");
        }
    }
}
