/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import cat.copernic.logica.RutaLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author alpep
 */
@Controller
@RequestMapping("/rutes")
public class RutaWebController {
      
    @Autowired
    private RutaLogic rutaLogic;  // Servicio para gestionar los clientes

    /**
     * Muestra la lista de todos las rutas en el sistema.
     *
     * @param model El modelo para pasar la lista de usuarios a la vista.
     * @param authentication
     * @return La vista con la lista de todos los usuarios.
     */
    
    @GetMapping("/all")
    public String ListAllRutes(Model model, Authentication authentication) {
        try {
            /*
            boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
            model.addAttribute("isAuthenticated", isAuthenticated);
           */
            model.addAttribute("rutes", rutaLogic.findAllRutes());
            return "rutes-list"; // Página que muestra la lista de clientes
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
}
