package com.example.demo.service;

import com.example.demo.model.Rol;
import com.example.demo.model.Solicitud;
import com.example.demo.model.UsuarioApp;
import com.example.demo.repository.SolicitudRepository;
import com.example.demo.repository.UsuarioAppRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public class SolicitudService {
    @Autowired
    private SolicitudRepository solicitudRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private UsuarioAppRepository usuarioAppRepository;

    public Solicitud crearSolicitud(Solicitud solicitud) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String username = auth.getName();
    UsuarioApp usuario = usuarioAppRepository.findByUsername(username);

    if (usuario.getRol() != Rol.ADMIN) {
        
        solicitud.setRegion(usuario.getRegion());
    }

    if (solicitud.getId() == null || solicitud.getId().isBlank()) {
        solicitud.setId(UUID.randomUUID().toString());
    }
    if (solicitud.getFecha() == null) {
        solicitud.setFecha(java.time.LocalDate.now());
    }
    // Setear usuario creador para relación JPA
    solicitud.setUsuarioCreador(usuario);
    Solicitud solicitudGuardada = solicitudRepository.save(solicitud);
    
    // Crear mensaje detallado para el email
    String mensajeDetallado = crearMensajeDetalladoHtml(solicitudGuardada);
    
    mailService.enviarCorreoHtml(solicitud.obtenerMailSoporte(), "Nueva solicitud de Celular", mensajeDetallado);
    if (solicitudGuardada.necesitaAutorizante()) {
        String mensajeAutorizante = crearMensajeAutorizanteHtml(solicitudGuardada);
        mailService.enviarCorreoHtml(solicitudGuardada.getMailAutorizante(), "Solicitud requiere autorización", mensajeAutorizante);
    }
    return solicitudGuardada;
}


    public List<Solicitud> getAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username     = auth.getName();   
        UsuarioApp usuario  = usuarioAppRepository.findByUsername(username);

          return usuario.getRol() == Rol.ADMIN
        ? solicitudRepository.findAll()
        : solicitudRepository.findAll().stream()
            .filter(s -> s.getRegion() == usuario.getRegion())
            .toList();
}

    public Solicitud cambiarEstado(String id, com.example.demo.model.EstadoSolicitud nuevoEstado) {
        Solicitud solicitud = solicitudRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        solicitud.cambiarEstado(nuevoEstado);
        return solicitudRepository.save(solicitud);
    }


    public List<Solicitud> getByUsuario(String usuario) {
        return solicitudRepository.findAll().stream()
            .filter(s -> usuario.equals(s.getUsuario()))
            .toList();
    }

    /**
     * Solicitudes creadas por el usuario autenticado (usando relación JPA usuarioCreador).
     */
    public List<Solicitud> getMiasCreadas() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Filtrar por el usuarioCreador (relación JPA) en lugar del campo texto 'usuario'
        return solicitudRepository.findAll().stream()
            .filter(s -> s.getUsuarioCreador() != null && s.getUsuarioCreador().getUsername().equals(username))
            .toList();
    }

    private String crearMensajeDetalladoHtml(Solicitud solicitud) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        html.append(".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        html.append(".header { background-color: #4CAF50; color: white; padding: 20px; text-align: center; }");
        html.append(".header h2 { margin: 0; font-size: 24px; }");
        html.append(".content { padding: 20px; }");
        html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        html.append("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        html.append("th { background-color: #f8f9fa; font-weight: bold; color: #333; width: 30%; }");
        html.append("td { color: #666; }");
        html.append("tr:hover { background-color: #f5f5f5; }");
        html.append(".footer { background-color: #e9ecef; padding: 15px; color: #666; text-align: center; }");
        html.append(".badge { background-color: #28a745; color: white; padding: 4px 8px; border-radius: 12px; font-size: 12px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h2>📱 Nueva Solicitud de Celular</h2>");
        html.append("</div>");
        
        html.append("<div class='content'>");
        html.append("<p>Se ha creado una nueva solicitud con los siguientes detalles:</p>");
        html.append("<table>");
        html.append("<tr><th>ID de Solicitud</th><td><strong>").append(solicitud.getId()).append("</strong></td></tr>");
        html.append("<tr><th>Fecha</th><td>").append(solicitud.getFecha()).append("</td></tr>");
        html.append("<tr><th>Tipo de Solicitud</th><td>").append(solicitud.getTipoSolicitud()).append("</td></tr>");
        html.append("<tr><th>Solicitante</th><td>").append(solicitud.getNomSolicitante()).append("</td></tr>");
        html.append("<tr><th>Usuario</th><td>").append(solicitud.getUsuario()).append("</td></tr>");
        html.append("<tr><th>Región</th><td>").append(solicitud.getRegion()).append("</td></tr>");
        html.append("<tr><th>Estado</th><td><span class='badge'>").append(solicitud.getEstado()).append("</span></td></tr>");
        html.append("<tr><th>Necesita Línea</th><td>").append(solicitud.isNecesitaLinea() ? "✅ Sí" : "❌ No").append("</td></tr>");
        
        if (solicitud.getMotivo() != null && !solicitud.getMotivo().isBlank()) {
            html.append("<tr><th>Motivo</th><td>").append(solicitud.getMotivo()).append("</td></tr>");
        }
        html.append("</table>");
        html.append("</div>");
        
        html.append("<div class='footer'>");
        html.append("💡 Puede revisar y procesar esta solicitud en el sistema de gestión de celulares.");
        html.append("</div>");
        html.append("</div>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }

    private String crearMensajeAutorizanteHtml(Solicitud solicitud) {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>");
        html.append("<html>");
        html.append("<head>");
        html.append("<meta charset='UTF-8'>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; background-color: #f5f5f5; }");
        html.append(".container { max-width: 600px; margin: 0 auto; background-color: white; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }");
        html.append(".header { background-color: #FF9800; color: white; padding: 20px; text-align: center; }");
        html.append(".header h2 { margin: 0; font-size: 24px; }");
        html.append(".content { padding: 20px; }");
        html.append(".urgent-notice { background-color: #fff3cd; border: 1px solid #ffeaa7; border-radius: 4px; padding: 15px; margin: 15px 0; }");
        html.append(".urgent-text { color: #856404; font-weight: bold; margin: 0; }");
        html.append("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        html.append("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        html.append("th { background-color: #fff3cd; font-weight: bold; color: #333; width: 30%; }");
        html.append("td { color: #666; }");
        html.append("tr:hover { background-color: #ffeaa7; }");
        html.append(".footer { background-color: #fff3cd; padding: 15px; color: #856404; text-align: center; font-weight: bold; }");
        html.append(".badge { background-color: #ff9800; color: white; padding: 4px 8px; border-radius: 12px; font-size: 12px; }");
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");
        
        html.append("<div class='container'>");
        html.append("<div class='header'>");
        html.append("<h2>⚠️ Autorización Requerida</h2>");
        html.append("</div>");
        
        html.append("<div class='content'>");
        html.append("<div class='urgent-notice'>");
        html.append("<p class='urgent-text'>🔔 ACCIÓN REQUERIDA: Se necesita su autorización para procesar la siguiente solicitud</p>");
        html.append("</div>");
        
        html.append("<table>");
        html.append("<tr><th>ID de Solicitud</th><td><strong>").append(solicitud.getId()).append("</strong></td></tr>");
        html.append("<tr><th>Fecha</th><td>").append(solicitud.getFecha()).append("</td></tr>");
        html.append("<tr><th>Tipo de Solicitud</th><td>").append(solicitud.getTipoSolicitud()).append("</td></tr>");
        html.append("<tr><th>Solicitante</th><td>").append(solicitud.getNomSolicitante()).append("</td></tr>");
        html.append("<tr><th>Usuario</th><td>").append(solicitud.getUsuario()).append("</td></tr>");
        html.append("<tr><th>Región</th><td>").append(solicitud.getRegion()).append("</td></tr>");
        html.append("<tr><th>Estado</th><td><span class='badge'>").append(solicitud.getEstado()).append("</span></td></tr>");
        html.append("<tr><th>Necesita Línea</th><td>").append(solicitud.isNecesitaLinea() ? "✅ Sí" : "❌ No").append("</td></tr>");
        
        if (solicitud.getMotivo() != null && !solicitud.getMotivo().isBlank()) {
            html.append("<tr><th>Motivo</th><td>").append(solicitud.getMotivo()).append("</td></tr>");
        }
        html.append("</table>");
        html.append("</div>");
        
        html.append("<div class='footer'>");
        html.append("⏰ Por favor, revise y autorice esta solicitud lo antes posible en el sistema de gestión de celulares.");
        html.append("</div>");
        html.append("</div>");
        
        html.append("</body>");
        html.append("</html>");
        
        return html.toString();
    }
}
