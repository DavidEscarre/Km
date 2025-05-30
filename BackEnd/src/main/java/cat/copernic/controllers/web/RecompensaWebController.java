/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import cat.copernic.Entity.Recompensa;
import cat.copernic.enums.EstatRecompensa;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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

      
            model.addAttribute("recompenses", recompenses.reversed());
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
    @GetMapping("/getById/{id}")
    public String ListAllRecompenses(@PathVariable("id") Long id, Model model, Authentication authentication) {
        try {
           Recompensa recompensa = recompensaLogic.getRecompensa(id);

      
            model.addAttribute("recompensa", recompensa);
            return "recompensa-details"; // Página que muestra la lista de clientes
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
    
    @GetMapping("/delete/{id}")
    public String deleteRecompensa(@PathVariable("id") Long recompensaId,
                                   Model model,
                                   Authentication authentication) {
        try{
            Recompensa recompensa = recompensaLogic.getRecompensa(recompensaId);
            if (recompensa == null) {
                model.addAttribute("errorMessage", "Recompensa no trobada: " + recompensaId);
                return "redirect:/recompenses";
            }
            if (recompensa.getEstat() != EstatRecompensa.DISPONIBLE) {
                model.addAttribute("errorMessage", "Només es poden eliminar recompenses en estat disponible.");
                return "redirect:/recompenses";
            }
            recompensaLogic.deleteRecompensa(recompensaId);
            return "redirect:/recompenses";
        }catch(Exception e){
             return "redirect:/recompenses";
        }
        
    }
    
    @PostMapping("/assignar/{id}")
    public String assignarRecompesa(@PathVariable("id") Long id, Model model, Authentication authentication) {
        
        
        try {
            Recompensa recompensa = recompensaLogic.getRecompensa(id);
            if (recompensa == null) {
                model.addAttribute("errorMessage", "Recompensa no trobada.");
                return "redirect:/recompenses/getById/" + id.toString();
            }else{
                
                String res = recompensaLogic.assignarRecompensa(id);
                if(res.equals("USER_NOT_FOUND")){
                    model.addAttribute("errorMessage", "Usuari recompensa no trobat.");
                    return "redirect:/recompenses/getById/" + id.toString();
                
                }else if(res.equals("RECOMPENSA_NO_RESERVADA")){
                    model.addAttribute("errorMessage", "La recompensa no esta reservada.");
                    return "redirect:/recompenses/getById/" + id.toString();
                }else if(res.equals("USER_SALDO_INSUFICIENT")){
                    model.addAttribute("errorMessage", "El ciclista de la recompensa no te actualment saldo suficient.");
                    return "redirect:/recompenses/getById/" + id.toString();
                }else if(res.equals(id.toString())){
                    model.addAttribute("errorMessage",null);
                    return "redirect:/recompenses/getById/" + id.toString();
                }else{
                    model.addAttribute("errorMessage",res);
                    return "redirect:/recompenses/getById/" + id.toString();
                    
                }
                
            }
           
            
            
        }catch(Exception e){
            return "redirect:/recompenses/getById/" + id.toString();
        }
            
    }
    
}
