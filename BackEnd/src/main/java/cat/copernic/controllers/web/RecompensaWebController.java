/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import cat.copernic.Entity.Recompensa;
import cat.copernic.logica.RecompensaLogic;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author alpep
 */
@Controller
@RequestMapping("/recompenses")
public class RecompensaWebController {
    
    @Autowired
    private RecompensaLogic recompensaLogic; 

    /**
     * Muestra la lista de todos las recompensas en el sistema.
     *
     * @param model El modelo para pasar la lista de usuarios a la vista.
     * @param authentication
     * @return La vista con la lista de todos las recompensas
     * 
     */ 
    
    @GetMapping
    public String ListAllRecompenses(Model model, Authentication authentication) {
        try {
           List<Recompensa> recompenses = recompensaLogic.findAllRecompenses();

      
            model.addAttribute("recompenses", recompenses);
            return "recompenses-list"; // Página que muestra la lista de clientes
        } catch (Exception e) {
            // Registra el error para depuración
            e.printStackTrace();
            if(authentication != null){
            model.addAttribute("authorities", authentication.getAuthorities());
            }
            model.addAttribute("errorMessage", "Ha ocurrido un error al cargar la lista de clientes.");
            return "error-page";  // Página personalizada de error
        }
    }
    
    @GetMapping("/create")
    public String createRecompesa(Model model, Authentication authentication) {
          model.addAttribute("recompensa", new Recompensa()); // Asegurar que el objeto está en el modelo
        return "create-recompensa";

        }
    
    @PostMapping("/create")
    public String createRecompesa(@ModelAttribute Recompensa recompensa, Model model, Authentication authentication) {
        try {
           if (recompensa == null) {
                model.addAttribute("errorMessage", "Camps incorrectes");
                return "redirect:/recompenses/create";
            }
           
            recompensa.setDataCreacio(LocalDateTime.now());
            recompensaLogic.createRecompensa(recompensa);
                
            return "redirect:/recompenses";
            
        }catch(Exception e){
            return "create-recompensa";
        }
            
        }
    
}
