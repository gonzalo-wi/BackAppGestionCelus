package com.example.demo;

import com.example.demo.model.UsuarioApp;
import com.example.demo.model.Rol;
import com.example.demo.model.Region;
import com.example.demo.repository.UsuarioAppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	@Autowired
	private UsuarioAppRepository usuarioAppRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Admin default
		if (usuarioAppRepository.findByUsername("admin") == null) {
			System.out.println("=== Creando usuario admin...");
			UsuarioApp admin = new UsuarioApp("admin", passwordEncoder.encode("admin123"), Rol.ADMIN, Region.NORTE);
			usuarioAppRepository.save(admin);
			System.out.println("=== Usuario admin creado con password: " + admin.getPassword());
		} else {
			UsuarioApp existingAdmin = usuarioAppRepository.findByUsername("admin");
			System.out.println("=== Usuario admin ya existe con password: " + existingAdmin.getPassword());
			boolean matches = passwordEncoder.matches("admin123", existingAdmin.getPassword());
			System.out.println("=== ¿La contraseña 'admin123' coincide? " + matches);
		}

		// USUARIO de pruebas para Región NORTE
		final String testUsername = "norte"; // login: norte / norte123
		if (usuarioAppRepository.findByUsername(testUsername) == null) {
			System.out.println("=== Creando usuario de pruebas '" + testUsername + "' (Region NORTE, Rol USUARIO)...");
			UsuarioApp userNorte = new UsuarioApp(testUsername, passwordEncoder.encode("norte123"), Rol.USUARIO, Region.NORTE);
			usuarioAppRepository.save(userNorte);
			System.out.println("=== Usuario '" + testUsername + "' creado");
		} else {
			System.out.println("=== Usuario de pruebas '" + testUsername + "' ya existe");
		}
	}

}
