/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import cat.copernic.Entity.Ruta;
import cat.copernic.Entity.Sistema;
import cat.copernic.logica.RutaLogic;
import cat.copernic.logica.SistemaLogic;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.UriUtils;

/**
 *
 * @author alpep
 */
@Controller
@RequestMapping("/rutes")
public class RutaWebController {
      
    @Autowired
    private RutaLogic rutaLogic; 
    
    @Autowired
    private SistemaLogic sistemaLogic;

    /**
     * Muestra la lista de todos las rutas en el sistema.
     *
     * @param model El modelo para pasar la lista de usuarios a la vista.
     * @param authentication
     * @return La vista con la lista de todos los usuarios.
     */
    
    @GetMapping
    public String ListAllRutes(Model model, Authentication authentication) {
        try {
           
            List<Sistema> sistemas = sistemaLogic.findAllSistemas();
            Sistema sistema ;
            if(sistemas.size() == 1 ){
                sistema = sistemas.getFirst();
            }else {
                sistema = new Sistema();
                sistema.setTempsMaxAtur(300000L);  
                sistema.setTempsMaxRec(259200000L); 
                sistema.setPrecisioPunts(2000L); 
                sistema.setPuntsKm(1.00);
                sistema.setVelMaxValida(60.00);

               // sistema.setId(sistemaLogic.findAllSistemas().getFirst().getId());
            }
            double velMax = sistema.getVelMaxValida();
           
            List<Ruta> rutas = rutaLogic.findAllRutes();
    
            if (rutas.isEmpty()) {
                // Si está vacía, puedes pasar un mensaje y devolver otra plantilla, por ejemplo "sin-rutas.html"
                model.addAttribute("mensajeError", "No hay rutas registradas para tu usuario.");
                return "rutes-list"; // Ahora existe la plantilla error-page.html
            }
            
            model.addAttribute("sistemaMaxVel", velMax);
            model.addAttribute("rutes", rutas);
            return "rutes-list"; 
        } catch (Exception e) {
       
            e.printStackTrace();
            if(authentication != null){
            model.addAttribute("authorities", authentication.getAuthorities());
            }
            model.addAttribute("errorMessage", "Ha ocurrido un error al cargar la lista de rutas.");
            return "error-page";  // Página personalizada de error
        }
    }
    
    @GetMapping("/getById/{id}")
    public String getRutaById(@PathVariable Long id, Model model, Authentication authentication) {
        try {
          
            Sistema sistema = sistemaLogic.findAllSistemas().getFirst();
            double velMax = sistema.getVelMaxValida();
            Ruta ruta = rutaLogic.getRuta(id);
            if(ruta == null){
                return "rutes-list";
            }
            Long tempsAturat = ruta.getTempsAturat();
              // Si falta cualquiera de las fechas, devolvemos un placeholder
            String tempsAturatFormat = "00:00:00"; 
            if (tempsAturat != null) {
                Duration duration = Duration.ofMillis(tempsAturat);

                long hours = duration.toHours();
                long minutes = duration.minusHours(hours).toMinutes();
                long seconds = duration
                        .minusHours(hours)
                        .minusMinutes(minutes)
                        .getSeconds();
                // Formateamos a "HH:mm:ss"
                tempsAturatFormat = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            }

           
            model.addAttribute("tempsAturat", tempsAturatFormat);
            model.addAttribute("sistemaMaxVel", velMax);
            model.addAttribute("ruta", ruta);
            model.addAttribute("coordenadas", ruta.getPuntsGPS()
                    .stream()
                    .map(p -> Arrays.asList(p.getLatitud(), p.getLongitud()))
                    .collect(Collectors.toList()));
            return "ruta-details"; // Página que muestra la lista de rutas
        } catch (Exception e) {
            // Registra el error para depuración
            e.printStackTrace();
            if(authentication != null){
            model.addAttribute("authorities", authentication.getAuthorities());
            }
            model.addAttribute("errorMessage", "Ha ocurrido un error al carregar els detalls de la ruta.");
            return "error-page";  // Página personalizada de error
        }
    }
    
    @PostMapping("/validar/{id}")
    public String Validar(@PathVariable Long id, Model model, Authentication authentication) {
        try {
          
            Ruta ruta = rutaLogic.validarRuta(id);
        
        
        
            return "redirect:/rutes/getById/" + id.toString();
        } catch (Exception e) {
       
            e.printStackTrace();
            if(authentication != null){
            model.addAttribute("authorities", authentication.getAuthorities());
            }
            model.addAttribute("errorMessage", "Ha ocurrido un error al cargar la lista de rutas.");
            return "error-page";  // Página personalizada de error
        }
    }
    
    @PostMapping("/invalidar/{id}")
    public String Invalidar(@PathVariable Long id, Model model, Authentication authentication) {
        try {
          
            Ruta ruta = rutaLogic.invalidarRuta(id);
        
        
        
            return "redirect:/rutes/getById/" + id.toString();
        } catch (Exception e) {
       
            e.printStackTrace();
            if(authentication != null){
            model.addAttribute("authorities", authentication.getAuthorities());
            }
            model.addAttribute("errorMessage", "Ha ocurrido un error al cargar la lista de rutas.");
            return "error-page";  // Página personalizada de error
        }
    }
    
    
    
}
