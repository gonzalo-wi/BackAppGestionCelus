package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.model.Celular;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UsuarioService {
    private final List<Usuario> usuarios = new ArrayList<>();

    public List<Usuario> getAll() {
        return usuarios;
    }

    public Usuario create(Usuario usuario) {
        usuarios.add(usuario);
        return usuario;
    }

    public void asignarCelular(Usuario usuario, Celular celular) {
        usuario.asignarCelular(celular);
        celular.asignarUsuario(usuario);
    }

    public Optional<Usuario> buscarPorNumReparto(String numReparto) {
        return usuarios.stream()
                .filter(u -> u.getNumReparto().equals(numReparto))
                .findFirst();
    }
}
