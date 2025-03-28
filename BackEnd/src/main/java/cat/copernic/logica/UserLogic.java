/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.logica;

import cat.copernic.Entity.User;
import cat.copernic.controllers.API.UserApiController;
import cat.copernic.enums.Rol;
import cat.copernic.repository.UserRepo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author alpep
 */
@Service
public class UserLogic {
    
    @Autowired
    private UserRepo userRepo;
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public List<User> findAllUsers() throws Exception{
        List<User> ret = new ArrayList<>();
        
      
           
        ret = userRepo.findAll();
        return ret;
        
    }
    
    public User getUser(String email){
        
        User ret = userRepo.findById(email).orElse(null);
        
        return ret;
    }
    public boolean existsByEmail(String email)
    {
        User p = userRepo.findById(email).orElse(null);
        
        return (p != null);
    }
    
    public boolean deleteUserByEmail(String email)
    {
        try{
            User p = userRepo.findById(email).orElse(null);
            
            userRepo.delete(p);
            
            return true;
        }catch(Exception e){
            return false;
        }
        
    }
    
    
    public String authenticateUser(String email, String rawPassword) {
        
       
        User user = userRepo.findById(email).orElse(null);
        if(user != null){
        if (passwordEncoder.matches(rawPassword, user.getWord())) {
            
            if (user.isEstat()== true) {
                System.out.println("✅ Autenticació exitosa");
                return user.getEmail();
            }
            else {
                System.out.println("❌ Usuari inactiu");
                return "INACTIVE";
            }
            
        }

        System.out.println("❌ Autenticació fallida");
        return "INCORRECT";
        }else{
            System.out.println("❌ Usuari no trobat");
        return "NOTFOUND";
        }
       
      }
    public String authenticateWebAdmin(String email, String rawPassword) {
        
       
        User user = userRepo.findById(email).orElse(null);
        if(user != null){
            if(user.getRol()== Rol.ADMIN){
                if (passwordEncoder.matches(rawPassword, user.getWord())) {

                    if (user.isEstat()== true) {
                        System.out.println("✅ Autenticació exitosa");
                        return user.getEmail();
                    }
                    else {
                        System.out.println("❌ Usuari inactiu");
                        return "INACTIVE";
                    }

                }else{
                    System.out.println("❌ Autenticació fallida");
                    return "INCORRECT";
                }
            }else{
                 return "NOADMIN";
            }

        }else{
            System.out.println("❌ Usuari no trobat");
        return "NOTFOUND";
        }
       
      }
    public boolean userIsUnique(User user){
        
        if(userRepo.existsById(user.getEmail())){
            return false;
            
        }else{
            return true;
        }
            
    }
    public String createUser(User user, MultipartFile imageFile) {
        
        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                user.setFoto(convertImageToBlob(imageFile));
            }
            
           // user.setWord(passwordEncoder.encode(user.getWord()));
            User savedUser = userRepo.save(user);
            return savedUser.getEmail();
        } catch (Exception e) {
            System.out.println("1111111111111111111111111111111111111111111111111111111111");
            return null;
        }

    }
    
    
        public byte[] convertImageToBlob(MultipartFile file) throws IOException {
        return file.getBytes();
    }
}

