/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import cat.copernic.Entity.User;
import cat.copernic.controllers.API.UserApiController;
import cat.copernic.logica.EmailService;
import cat.copernic.logica.UserLogic;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import static org.hibernate.internal.CoreLogging.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;

/**
 *
 * @author alpep
 */

@Controller
@RequestMapping("/login")
public class AuthWebController {
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserLogic userLogic;
            
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
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
    
    
    // Nuevo método para cerrar sesión
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // Invalidar la sesión
        request.getSession().invalidate();
        
        // Borrar la autenticación
        SecurityContextHolder.clearContext();

        logger.info("Usuari desconnectat correctament");
        return "redirect:/login?logout"; // Redirigir al login después del logout
    }
    
    
    
    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        try{
           
            logger.info(email.substring(1, email.length()-1));
            model.addAttribute("errorMessage", null);
           // User user = userLogic.getUser(email.substring(1, email.length()-1));
           
            String cleanEmail = email.trim().replaceAll("^\"|\"$", "");
            User user = userLogic.getUser(cleanEmail);
            if (user == null) {
                model.addAttribute("errorMessage", "Email Incorrecte.");
                return "forgotPassword";
            }
          
            String token = userLogic.tokenGenerator();
            user.setResetToken(token);
            user.setTokenExpiration(LocalDateTime.now().plusMinutes(5));
            String res = userLogic.updateUser(user);
            if(res==null){
                model.addAttribute("errorMessage", "Email Incorrecte.");
                return "redirect:/login/forgotPassword";
            }else if(res.equals("NOT_FOUND")){
                model.addAttribute("errorMessage", "Email Incorrecte.");
                logger.error("no trobat");
                return "redirect:/login/forgotPassword";
            }else{
                
                model.addAttribute("errorMessage", null);
                // Lógica para enviar email aquí
                emailService.sendPasswordResetEmail(user.getEmail(), token);
 
                return "redirect:/login/verifyToken/" + UriUtils.encode(cleanEmail, StandardCharsets.UTF_8);
            }
            
        }catch(Exception e){
            logger.error("exception"+e.getMessage());
            return "redirect:/login/forgotPassword";
        }
        
    }
    
    @PostMapping("/verifyToken")
    public String verifyToken(@RequestParam("email") String email, @RequestParam("token") String token, Model model) {
       
        try{
            logger.info(email);
            model.addAttribute("email", email);
            User user = userLogic.getUser(email);
            if (user == null) {
                logger.error("Usuari primera verificacio no trobat.");
                model.addAttribute("errorMessage", "S'ha produit un error intern, Usuari no trobat... Torna a intentarlo.");
                return "verifyToken";
            }
            String res = userLogic.verifyToken(user.getEmail(), token);
            
            if(res == null){
                model.addAttribute("errorMessage", "S'ha produit un error intern, Usuari no trobat... Torna a intentarlo.");
                return "verifyToken";
            }else if(res.equals("EXPIRED_TOKEN")){
                model.addAttribute("errorMessage", "El token introduit a expirat.");
                logger.error("verificacio token fallida user: "+user.getEmail()+", Token EXPIRAT.");
               
               return "verifyToken";
            }else if(res.equals("INCORRECT_TOKEN")){
                logger.error("verificacio token fallida user: "+user.getEmail()+", Token INCORRECTE.");
                model.addAttribute("errorMessage", "El token introduit es incorrecte.");

                return "verifyToken";
            }else if(res.equals(email)){
                logger.info("verificacio token existosa user: "+user.getEmail()+", Token CORRECTE.");
                 return "redirect:/login/recoverPassword/" + UriUtils.encode(email, StandardCharsets.UTF_8);
            }else{
                model.addAttribute("errorMessage", "S'ha produit un error intern, Usuari no trobat... Torna a intentarlo.");
                return "verifyToken";
            }
           
            
            
        }catch(Exception e){
            logger.error("Exception token verifycation apiRest: "+e.getMessage());
            model.addAttribute("errorMessage", "S'ha produit un error intern, Usuari no trobat... Torna a intentarlo.");
            return "verifyToken";
        }
        
    }
    
    @PostMapping("/recoverPassword")
    public String recoverPassword(@RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword, Model model) {
       
        try{
            logger.info("recover password puta: "+email);
            model.addAttribute("email", email);
            User user = userLogic.getUser(email);
            if (user == null) {
                logger.error("Usuari primera verificacio no trobat.");
                model.addAttribute("errorMessage", "S'ha produit un error intern, Usuari no trobat... Torna a intentarlo.");
                return "recoverPassword";
            }
            if(!password.equals(confirmPassword)){
                model.addAttribute("errorMessage", "Les contrasenyes han de coincidir.");
                //return "redirect:/login/recoverPassword/" + UriUtils.encode(email, StandardCharsets.UTF_8);
                return "recoverPassword";
            }
            user.setWord(passwordEncoder.encode(password));
            String res = userLogic.updateUser(user);
            
            if(res == null){
                model.addAttribute("errorMessage", "S'ha produit un error intern, Usuari no trobat... Torna a intentarlo.");
                return "recoverPassword";
            }else if(res.equals(email)){
                logger.info("contrasenya actualitzada existosament de user: "+user.getEmail()+".");
                return "redirect:/login/successRecoverPassword";
            }else{
                model.addAttribute("errorMessage", "S'ha produit un error intern, Usuari no trobat... Torna a intentarlo.");
                return "recoverPassword";
            }
 
        }catch(Exception e){
            logger.error("Exception token verifycation apiRest: "+e.getMessage());
            model.addAttribute("errorMessage", "S'ha produit un error intern, Usuari no trobat... Torna a intentarlo.");
            return "recoverPassword";
        }
    }
    
    @GetMapping("/forgotPassword")
    public String forgotPassword(Model model) {
        return "forgotPassword"; // Vista del login
    }
    
    @GetMapping("/verifyToken/{email}")
    public String verifyToken(@PathVariable("email") String email, Model model) {
        // Añadimos directamente el email al modelo
        model.addAttribute("email", email);
        return "verifyToken";
    }
    
    @GetMapping("/recoverPassword/{email}")
    public String recoverPassword(@PathVariable("email") String email, Model model) {
        // Añadimos directamente el email al modelo
        model.addAttribute("email", email);
        return "recoverPassword";
    }
    
    @GetMapping("/successRecoverPassword")
    public String successRecoverPassword(Model model) {
        return "successRecoverPassword"; // Vista del login
    }
    
    
}