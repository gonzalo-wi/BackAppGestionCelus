package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173", "http://127.0.0.1:5173"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setAllowCredentials(true);
    configuration.setExposedHeaders(Arrays.asList("Authorization"));
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      // CORS configuración
      .cors(cors -> cors.configurationSource(corsConfigurationSource()))
      // Deshabilitar CSRF para APIs
      .csrf(csrf -> csrf.disable())
      // Permitir frames para H2 Console (nueva sintaxis)
      .headers(headers -> headers
          .frameOptions(frameOptions -> frameOptions.disable()))
      
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/h2-console/**").permitAll()       // consola H2
        .requestMatchers("/api/solicitudes/**").hasAnyRole("ADMIN", "USUARIO") // solicitudes requieren autenticación
        .requestMatchers("/api/usuarios/mi-flota").hasAnyRole("ADMIN","USUARIO") // mi flota para ambos roles
        .requestMatchers("/api/usuarios/me/solicitudes").hasAnyRole("ADMIN","USUARIO") // mis solicitudes para ambos roles
        .requestMatchers("/api/usuarios/flota/*/linea").hasAnyRole("ADMIN", "USUARIO") // editar línea de flota para ambos roles
        .requestMatchers("/api/estadisticas/mi-region").hasAnyRole("ADMIN", "USUARIO") // estadísticas de mi región
        .requestMatchers("/api/estadisticas/totales").hasAnyRole("ADMIN", "USUARIO") // estadísticas totales para ambos roles
        .requestMatchers("/api/estadisticas/reportes/**").hasAnyRole("ADMIN", "USUARIO") // reportes para ambos roles
        .requestMatchers("/api/estadisticas/todas-regiones").hasRole("ADMIN") // todas las regiones solo para admin
        .requestMatchers("/api/estadisticas/region/**").hasRole("ADMIN") // región específica solo para admin
        .requestMatchers("/api/admin/usuarios-sistema/**").hasRole("ADMIN") // gestión de usuarios del sistema solo para admin
        .requestMatchers("/api/celulares/gestion/**").hasRole("ADMIN") // gestión avanzada de celulares solo para admin
        .requestMatchers("/api/movimientos/**").hasAnyRole("ADMIN", "USUARIO")  // movimientos para ambos roles
        .requestMatchers("/api/celulares/**").hasAnyRole("ADMIN", "USUARIO")    // celulares para ambos roles  
        .requestMatchers("/api/usuarios/**").hasRole("ADMIN")     // resto de usuarios solo para admin
        .requestMatchers("/api/**").hasRole("ADMIN")         // otros endpoints solo para admin
        .anyRequest().permitAll()
      )
      .httpBasic(Customizer.withDefaults()); // Basic Auth

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
