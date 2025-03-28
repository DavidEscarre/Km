/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import cat.copernic.Entity.User;
import cat.copernic.controllers.API.UserApiController;
import cat.copernic.logica.UserLogic;
import static org.hibernate.internal.CoreLogging.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author alpep
 */

@Controller
@RequestMapping("/login")
public class AuthWebController {
    
    @Autowired
    private UserLogic userLogic;
            
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
    @PostMapping
    public String LoginForm(@RequestParam("username") String username, @RequestParam("password") String password, Model model) {
    
       // System.out.println("Iniciando sesión con usuario: " + username);

       String res =  userLogic.authenticateWebAdmin(username, password);
       model.addAttribute("errorMessage", null);
        System.out.println("11111111111111111111111111111111111111  "+username+"sssssssssssssssssss +");
        if(res.equals("NOTFOUND")){
            logger.error("usuari no trobat ");
            model.addAttribute("errorMessage", "Usuari no trobat.");
            return "redirect:/login?error=notfound";
        }
        else if (res.equals("INCORRECT")) {
            logger.info("credencials incorrectes ");
            model.addAttribute("errorMessage", "Credencials incorrectes.");
           return "redirect:/login?error=incorrect";

        }
        else if(res.equals("INACTIVE")){
            logger.info("usuari no actiu");
            model.addAttribute("errorMessage", "Usuari inactiu");
           return "redirect:/login?error=inactive";

        }else if(res.equals("NOADMIN")){
            logger.info("usuari no es admin");
            model.addAttribute("errorMessage", "Usuari no permes");
             return "redirect:/login?error=noadmin";
        }else{
        
            
            logger.info("usuari trobat i credencials correctes, autenticat");
            model.addAttribute("errorMessage", null);

            
        }
        return "redirect:/";
    }
    
    @GetMapping
    public String redirigirLoging(Model model) {
        return "login"; // Vista del login
    }
}