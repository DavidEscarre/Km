/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import cat.copernic.Entity.User;
import cat.copernic.enums.Rol;
import cat.copernic.logica.UserLogic;
import java.time.LocalDateTime;
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
@RequestMapping("/users")
public class UserWebController {
    
    @Autowired
    private UserLogic userLogic;  // Servicio para gestionar los clientes

    /**
     * Muestra la lista de todos los usuarios en el sistema.
     *
     * @param model El modelo para pasar la lista de usuarios a la vista.
     * @param authentication
     * @return La vista con la lista de todos los usuarios.
     */
    
    @GetMapping("/all")
    public String ListAllUsers(Model model, Authentication authentication) {
        try {
            /*
            boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
            model.addAttribute("isAuthenticated", isAuthenticated);
           */
            model.addAttribute("users", userLogic.findAllUsers());
            return "users-list"; // Página que muestra la lista de clientes
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
    public String createUsers(Model model, Authentication authentication) {
          model.addAttribute("user", new User()); // Asegurar que el objeto está en el modelo
        return "create-user";

        }
    @PostMapping("/create")
    public String createUsers(@ModelAttribute User user, Model model, Authentication authentication) {
        try {
           
         // Instanciar el codificador de contraseñas BCrypt
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // Encriptar la contraseña
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setWord(encryptedPassword);
            user.setRol(Rol.CICLISTA);
            user.setDataAlta(LocalDateTime.now());
            user.setSaldoDisponible(0.00);
            userLogic.createUser(user,null);
            return "redirect:/users/all";
        }catch(Exception e){
            return "create-user";
        }
            
        }
}
