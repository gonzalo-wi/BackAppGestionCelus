package com.example.demo.service;

import com.example.demo.model.UsuarioApp;
import com.example.demo.repository.UsuarioAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioAppRepository usuarioAppRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("=== UserDetailsService: Buscando usuario: " + username);
        
        UsuarioApp usuario = usuarioAppRepository.findByUsername(username);
        if (usuario == null) {
            System.out.println("=== Usuario no encontrado: " + username);
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }

        System.out.println("=== Usuario encontrado: " + usuario.getUsername());
        System.out.println("=== Password hash: " + usuario.getPassword());
        System.out.println("=== Rol: " + usuario.getRol());

        return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(Arrays.asList(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())))
                .build();
    }
}