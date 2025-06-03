/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.User;
import cat.copernic.logica.UserLogic;
import cat.copernic.repository.UserRepo;
import jakarta.annotation.security.PermitAll;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
@RequestMapping("/rest/users")
public class UserApiController {
    
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
      
    @Autowired
    private UserLogic userLogic;
    
    @Autowired
    private UserRepo userRepo;
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll(){
 
        List<User> llista;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");
        
        try {
            
            llista = userLogic.findAllUsers();
            logger.info("Trobada llista de tots els usuaris", "trobats: "+llista.size());
            return new ResponseEntity<>(llista, headers, HttpStatus.OK);
            
        } catch (Exception e) {
            logger.error("Llista de tots els usuaris no trobada, error del servidor", "Error message: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        
    }
    
    @GetMapping("/byEmail/{userEmail}")
    public ResponseEntity<User> getByEmail(@PathVariable String userEmail){
        
        User user;
        
        ResponseEntity<User> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        
        
      
        try {
            
            user = userLogic.getUser(userEmail);
            logger.info("Cercant usuari amb email: {}", userEmail);
          
            
            if (user == null)
            {
                logger.info("Usuari no trobat: {}", userEmail);       
                response = new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }
            else
            {
                logger.info("Usuari trobat: {}", userEmail);    
                response = new ResponseEntity<>(user, headers, HttpStatus.OK);
            }
            
        } catch (Exception e) {
           
            logger.error("Error intern del servidor al intentar trobar l'usuari {}", "Error mesage: "+e.getMessage());
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;
    }
    

    
    @DeleteMapping("/delete/{userEmail}")
    public ResponseEntity<Void> deleteUserByEmail(@PathVariable String userEmail){
        
        ResponseEntity<Void> response;
        
        try {
            if (userLogic.existsByEmail(userEmail))
            {
                userLogic.deleteUserByEmail(userEmail);
                response = ResponseEntity.noContent().build();
            }
            else
                response = ResponseEntity.notFound().build();
            
            
        } catch (Exception e) {
            
            response = ResponseEntity.internalServerError().build();
        }
        
        return response;
    }
    @PutMapping("/updatePassword/{userEmail}")
    @PermitAll
    public ResponseEntity<User> updatePassword(@PathVariable String userEmail, @RequestBody String password) {
       
        ResponseEntity<User> response;
        logger.info("User ID from URL: " + userEmail);
        try {
            User existingUser = userLogic.getUser(userEmail);

            if (existingUser != null) {
                User updatedUser = existingUser;
                updatedUser.setWord(passwordEncoder.encode(password.substring(1, password.length()-1)));
            userLogic.updateUser(updatedUser);

              
                return new ResponseEntity<>(existingUser, HttpStatus.OK);
            }else{
                 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("ERROR UPDATE: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PutMapping("/update/{userEmail}")
    @PermitAll
    public ResponseEntity<User> update(@PathVariable String userEmail, @RequestBody User updatedUser) {
        ResponseEntity<User> response;

        
        logger.info("User ID from URL: " + userEmail);
        logger.info("Updated User Object: " + updatedUser);

        try {
            Optional<User> existingUserOptional = userRepo.findById(userEmail);

            if (existingUserOptional.isPresent()) {
                String res = userLogic.updateUser(updatedUser);
                if(res.equals("NOT_FOUND")){
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }else if(res.equals(userEmail)){
                      User user = userLogic.getUser(userEmail);
                      logger.error("UPDATED USER: "+userEmail);
                      return new ResponseEntity<>(user, HttpStatus.OK);
                }else{
                     logger.error("ERROR UPDATE InternalServer Error in UserLogic");
                     return ResponseEntity.internalServerError().build();
                }
              
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
               
            }


        } catch (Exception e) {
            logger.error("ERROR UPDATE: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
   /* @PutMapping("/update-profile-image/{userEmail}")
    public ResponseEntity<?> updateProfileImage(@PathVariable String userEmail, @RequestParam("image") MultipartFile imageFile) {
        try{
            User user = userLogic.getUser(userEmail);
            if(user != null){
                   userLogic.updateUserAndImage(user, imageFile);
                return ResponseEntity.ok(user); // Devuelve el usuario actualizado en JSON
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
        }catch(Exception e){
            logger.error("ERROR UPDATE: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
        
     
    }   */
    
    
    
}
