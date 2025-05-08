/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.User;
import cat.copernic.enums.Rol;
import cat.copernic.logica.EmailService;
import cat.copernic.logica.UserLogic;
import cat.copernic.repository.UserRepo;
import jakarta.annotation.security.PermitAll;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author alpep
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")  // Permitir acceso desde cualquier origen
public class AuthApiController {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private UserLogic userLogic;
    
    @Autowired
    private UserRepo userRepo;
    
    Logger logger = LoggerFactory.getLogger(AuthApiController.class);
    
        
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String email, @RequestParam String word) {
         
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); 
        
        String res = userLogic.authenticateUser(email, word);

        User user;
        if(res.equals("NOTFOUND")){
            logger.error("usuari no trobat ");
           // return ResponseEntity.status(401).body("Usuari no trobat");
           return ResponseEntity.status(404).body(null); // 404 de not found
        }
        else if (res.equals("INCORRECT")) {
            logger.info("credencials incorrectes ");
            //return ResponseEntity.status(401).body("Credencials incorrectes");
            return ResponseEntity.status(401).body(null); // 403 de unauthorithed o algo asi
        }
        else if(res.equals("INACTIVE")){
            logger.info("usuari no actiu");
           // return ResponseEntity.badRequest().body("Usuari no activat");
           return ResponseEntity.status(403).body(null); // 403 de forbiden
        }else{
             user = userLogic.getUser(res);
             if(user != null){
                    logger.info("usuari trobat i credencials correctes, autenticat");
                  return ResponseEntity.ok(user); 
             }else{
                  logger.error("usuari no trobat en la segona verificació");
                 //return ResponseEntity.status(401).body("Usuari no trobat en segona veirificació");
                 return ResponseEntity.status(401).body(null);
             }
           
        }
            
    }
    
    
    @PostMapping("/forgotPassword")
    @PermitAll
    public ResponseEntity<String> forgotPassword(@RequestBody String email) {
        try{
             logger.info(email.substring(1, email.length()-1));
             HttpHeaders headers = new HttpHeaders();
             headers.add("Cache-Control", "no-store"); 
            User user = userLogic.getUser(email.substring(1, email.length()-1));
            if (user == null) {
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }
          
            String token = userLogic.tokenGenerator();
            user.setResetToken(token);
            user.setTokenExpiration(LocalDateTime.now().plusMinutes(5));
            String res = userLogic.updateUser(user);
            if(res==null){
                return ResponseEntity.internalServerError().body("InternalServerError");
            }else if(res.equals("NOT_FOUND")){
                 logger.error("no trobat");
                 return ResponseEntity.status(404).body(null); // 404 de not found
            }else{
                // Lógica para enviar email aquí
                emailService.sendPasswordResetEmail(user.getEmail(), token);
 
                return ResponseEntity.ok("Email enviado");
            }
            
        }catch(Exception e){
            logger.error("exception"+e.getMessage());
            return null;
        }
        
    }
    
    @PostMapping("/verifyToken")
    @PermitAll
    public ResponseEntity<?> verifyToken(@RequestParam String email, @RequestParam String token) {
       
        try{
            logger.info(email);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-store"); 
            User user = userLogic.getUser(email);
            if (user == null) {
                logger.error("Usuari primera verificacio no trobat.");
                return ResponseEntity.badRequest().body("Usuario no encontrado");
            }
            String res = userLogic.verifyToken(user.getEmail(), token);
            
            if(res == null){
                return ResponseEntity.internalServerError().body("NOT_FOUND");
            }else if(res.equals("EXPIRED_TOKEN")){
                logger.error("verificacio token fallida user: "+user.getEmail()+", Token EXPIRAT.");
                return ResponseEntity.status(401).body("EXPIRED_TOKEN"); // 401 de Unauthorized porque no es valido
            }else if(res.equals("INCORRECT_TOKEN")){
                logger.error("verificacio token fallida user: "+user.getEmail()+", Token INCORRECTE.");
                return ResponseEntity.status(401).body("INCORRECT_TOKEN"); // 401 de Unauthorized porque no es valido
            }else if(res.equals(email)){
                logger.info("verificacio token existosa user: "+user.getEmail()+", Token CORRECTE.");
                return ResponseEntity.ok().body("CORRECT_TOKEN");
            }else{
                return ResponseEntity.internalServerError().body("Unespected Error");
            }
           
            
            
        }catch(Exception e){
            logger.error("Exception token verifycation apiRest: "+e.getMessage());
            return ResponseEntity.internalServerError().body("EXCEPTION: "+e.getMessage());
        }
        
    }
        
    
}   