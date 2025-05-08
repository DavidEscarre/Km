/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import cat.copernic.Entity.Recompensa;
import cat.copernic.Entity.Ruta;
import cat.copernic.Entity.User;
import cat.copernic.enums.Rol;
import cat.copernic.logica.RecompensaLogic;
import cat.copernic.logica.RutaLogic;
import cat.copernic.logica.UserLogic;
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
@RequestMapping("/users")
public class UserWebController {
    
    @Autowired
    private UserLogic userLogic;  // Servicio para gestionar los clientes

    @Autowired
    private RecompensaLogic recompensaLogic;  
    
    @Autowired
    private RutaLogic rutaLogic;  
    /**
     * Muestra la lista de todos los usuarios en el sistema.
     *
     * @param model El modelo para pasar la lista de usuarios a la vista.
     * @param authentication
     * @return La vista con la lista de todos los usuarios.
     */
    
    @GetMapping
    public String ListAllUsers(Model model, Authentication authentication) {
        try {
           List<User> users = userLogic.findAllUsers();
           Map<String, String> userImages = new HashMap<>();
            
            for (User user : users) {
                byte[] imageData = user.getFoto();
                if (imageData != null) {
                    String base64Image = Base64.getEncoder().encodeToString(imageData);
                    userImages.put(user.getEmail(), base64Image);
                }
            }
            
            /*
            boolean isAuthenticated = authentication != null && authentication.isAuthenticated();
            model.addAttribute("isAuthenticated", isAuthenticated);
           */
            model.addAttribute("userImages",userImages);
            model.addAttribute("users", users);
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
    public String createUsers(@ModelAttribute User user, @RequestParam("confirmWord") String confirmWord ,@RequestParam("image") MultipartFile imageFile ,Model model, Authentication authentication) {
        try {
           if (user == null || !confirmWord.equals(user.getPassword())) {
                model.addAttribute("errorMessage", "Camps incorrectes");
                return "redirect:/users/create";
            }
            if (userLogic.userIsUnique(user)) {
                
                    // Instanciar el codificador de contraseñas BCrypt
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                // Encriptar la contraseña
                String encryptedPassword = passwordEncoder.encode(user.getPassword());
                user.setWord(encryptedPassword);
                
                user.setDataAlta(LocalDateTime.now());
                user.setSaldoDisponible(0.00);
                userLogic.createUser(user,imageFile);
                
                return "redirect:/users";
            } else {
                model.addAttribute("errorMessage", "Usuari ya existent");
                return "redirect:/users/create";
            }
        }catch(Exception e){
            return "create-user";
        }
            
    }
    
    @GetMapping("/getByEmail/{email}")
    public String veureDetallsUsuari(@PathVariable String email, Model model) {
    try {
        User usuari = userLogic.getUser(email);
        
        List<Recompensa> recompensas = recompensaLogic.getAllByCiclistaEmail(email);
        List<Ruta> rutas = rutaLogic.getAllByCiclistaEmail(email);
        
        model.addAttribute("recompensas", recompensas);
        model.addAttribute("rutas", rutas);
        if (usuari == null) {
            model.addAttribute("errorMessage", "Usuari no trobat.");
            return "users-list";
        }

        // Si tiene foto, convertirla a Base64
        if (usuari.getFoto() != null) {
            
            String base64Image = Base64.getEncoder().encodeToString(usuari.getFoto());
            model.addAttribute("userImage", base64Image);
        }
        

        model.addAttribute("usuari", usuari);
        return "user-details";  // nombre del archivo HTML
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("errorMessage", "Error al carregar els detalls de l’usuari.");
        return "users-list";
    }
}
    

    
}
