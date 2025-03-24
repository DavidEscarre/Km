/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.User;
import cat.copernic.logica.UserLogic;
import cat.copernic.repository.UserRepo;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    
    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll(){
 
        List<User> llista;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");
        
        try {
            
            llista = userLogic.findAllUsers();
            return new ResponseEntity<>(llista, headers, HttpStatus.OK);
            
        } catch (Exception e) {
           
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        
    }
    
    @GetMapping("/byEmail/{userEmail}")
    public ResponseEntity<User> getByEmail(@PathVariable String userEmail){
        
        User user;
        
        ResponseEntity<User> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        
        logger.info("Buscando usuario con email: {}", userEmail);
        user = userLogic.getUser(userEmail);
        logger.info("Usuario encontrado: {}", user);
        try {
            
            user = userLogic.getUser(userEmail);
            logger.info("Usuario encontrado: {}", user);       
            
            if (user == null)
            {
                response = new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }
            else
            {
                response = new ResponseEntity<>(user, headers, HttpStatus.OK);
            }
            
        } catch (Exception e) {
           
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;
    }
    
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody User user) throws IOException {
        
        
        
        // Ruta de la imagen en el sistema de archivos
        Path projectPath = Paths.get("").toAbsolutePath();

        Path imagePath = projectPath.resolve("src/main/java/cat/copernic/mavenproject1/tux.jpg");
        byte[] imageBytes = Files.readAllBytes(imagePath);

        // Convertir a MultipartFile simulado
        MultipartFile imageFile = new MockMultipartFile("imagen.jpg", imageBytes);
        
        try {
            
            if (user == null)
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else
            {
                
                if (userLogic.userIsUnique(user)){
                    String userEmail = userLogic.createUser(user, imageFile);
                    return new ResponseEntity<>(userEmail, HttpStatus.CREATED);
                }else{
                    return new ResponseEntity<>(HttpStatus.IM_USED);
                }
                
            }
    
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    
    
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String userEmail){
        
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
    /*
    @PutMapping("/update/{userId}")
    public ResponseEntity<User> update(@PathVariable Long userId, @RequestBody User updatedUser) {
        ResponseEntity<User> response;

        
        logger.info("User ID from URL: " + userId);
        logger.info("Updated User Object: " + updatedUser);

        try {
            Optional<User> existingUserOptional = userRepo.findById(userId);

            if (existingUserOptional.isPresent()) {
                User existingUser = existingUserOptional.get();

                // Actualizar solo los campos necesarios
                existingUser.setName(updatedUser.getName());
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
                existingUser.setImage(updatedUser.getImage());
                existingUser.setStatus(updatedUser.isStatus());
                existingUser.setRole(updatedUser.getRole());

                userRepo.save(existingUser);
                return new ResponseEntity<>(existingUser, HttpStatus.OK);
            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            logger.error("ERROR UPDATE: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }*/
}
