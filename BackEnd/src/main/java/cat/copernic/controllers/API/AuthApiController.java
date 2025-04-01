/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.User;
import cat.copernic.enums.Rol;
import cat.copernic.logica.UserLogic;
import cat.copernic.repository.UserRepo;
import static java.time.LocalDateTime.now;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
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
public class AuthApiController {

    @Autowired
    private UserLogic userLogic;
    
    @Autowired
    private UserRepo userRepo;
    
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
        
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestParam String email, @RequestParam String word) {
         
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); 
        
        String res = userLogic.authenticateUser(email, word);

        User user;
        if(res.equals("NOTFOUND")){
            logger.error("usuari no trobat ");
           // return ResponseEntity.status(401).body("Usuari no trobat");
           return ResponseEntity.status(401).body(null);
        }
        else if (res.equals("INCORRECT")) {
            logger.info("credencials incorrectes ");
            //return ResponseEntity.status(401).body("Credencials incorrectes");
            return ResponseEntity.status(401).body(null);
        }
        else if(res.equals("INACTIVE")){
            logger.info("usuari no actiu");
           // return ResponseEntity.badRequest().body("Usuari no activat");
           return ResponseEntity.badRequest().body(null);
        }else{
             user = userRepo.findById(res).orElse(null);
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
        
    
}