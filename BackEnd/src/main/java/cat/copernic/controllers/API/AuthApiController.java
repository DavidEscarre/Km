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
import org.springframework.beans.factory.annotation.Autowired;
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
    
    
    
        
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
        
        
        String res = userLogic.authenticateUser(email, password);

        User user;
        if(res.equals("NOTFOUND")){
            return ResponseEntity.status(401).body("Usuari no trobat");
        }
        else if (res.equals("INCORRECT")) {
            return ResponseEntity.status(401).body("Credencials incorrectes");
        }
        else if(!res.equals("INACTIVE")){
            return ResponseEntity.badRequest().body("Usuari no activat");
        }else{
             user = userRepo.getById(res);
            return ResponseEntity.ok(user);  // Enviar los datos del usuario si es correcto
        }
            
    }
        
    
    
        @PostMapping("/register")
        public ResponseEntity<?> registerUser(@RequestParam String nom, @RequestParam String email,
                @RequestParam int telefon, @RequestParam String word,
                @RequestParam String adreca, @RequestParam String observacions,
                @RequestParam(value = "image", required = false) MultipartFile imageFile) {

            User createdUser = new User (email, nom, observacions, Rol.CICLISTA, true, 0.00, now(), word, null, telefon, adreca);

            if (userLogic.userIsUnique(createdUser)) {
                String res = userLogic.createUser(createdUser, imageFile);
                if(res!=null){
                    return ResponseEntity.ok(createdUser);
                }else{
                    return ResponseEntity.internalServerError().body("Usuari no creat");
                }
                
            }else{
                 return ResponseEntity.status(401).body("Email ya existemt");
            }

           
        }
}