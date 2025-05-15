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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
          
            return null;
        }

    }
    public String updateUserAndImage(User newUser, MultipartFile imageFile) {
        
        try {
            User oldUser = userRepo.findById(newUser.getEmail()).orElse(null);
            if(oldUser == null){
                return "NOT_FOUND";
            }
            if (imageFile != null && !imageFile.isEmpty()) {
                newUser.setFoto(convertImageToBlob(imageFile));
                oldUser.setFoto(newUser.getFoto());
            }
            oldUser.setAdreca(newUser.getAdreca());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setEstat(newUser.isEstat());
            oldUser.setNom(newUser.getNom());
            oldUser.setObservacions(newUser.getObservacions());
            oldUser.setRecompensas(newUser.getRecompensas());
            oldUser.setResetToken(newUser.getResetToken());
            oldUser.setRol(newUser.getRol());
            oldUser.setRutes(newUser.getRutes());
            oldUser.setSaldoDisponible(newUser.getSaldoDisponible());
            oldUser.setTelefon(newUser.getTelefon());
            oldUser.setTokenExpiration(newUser.getTokenExpiration());
            oldUser.setWord(newUser.getWord());
           // user.setWord(passwordEncoder.encode(user.getWord()));
            User savedUser = userRepo.save(oldUser);
            return savedUser.getEmail();
        } catch (Exception e) {
           
            return null;
        }

    }
    
    public String updateUser(User newUser) {
        
        try {
            User oldUser = userRepo.findById(newUser.getEmail()).orElse(null);
            if(oldUser == null){
                return "NOT_FOUND";
            }
            
            oldUser.setAdreca(newUser.getAdreca());
            oldUser.setEmail(newUser.getEmail());
            oldUser.setEstat(newUser.isEstat());
            oldUser.setNom(newUser.getNom());
            oldUser.setObservacions(newUser.getObservacions());
          
            oldUser.setResetToken(newUser.getResetToken());
            oldUser.setRol(newUser.getRol());
        
            oldUser.setSaldoDisponible(newUser.getSaldoDisponible());
            oldUser.setTelefon(newUser.getTelefon());
            oldUser.setTokenExpiration(newUser.getTokenExpiration());
            oldUser.setWord(newUser.getWord());
            if(newUser.getFoto() != null){
                 oldUser.setFoto(newUser.getFoto());
            }
            User savedUser = userRepo.save(oldUser);
            return savedUser.getEmail();
        } catch (Exception e) {
            return null;
        }

    }
    
    public String verifyToken (String email, String token)throws Exception{
        
        
        User user = userRepo.findById(email).orElse(null);
        if(user != null){
            if((user.getResetToken().equals(token))){
               if(user.getTokenExpiration().isAfter(LocalDateTime.now())){
                    user.setResetToken(null);
                    user.setTokenExpiration(null);
                    logger.info("token verificat.");
                    User saverUser = userRepo.save(user);
                    return saverUser.getEmail();
               }else{
                   return "EXPIRED_TOKEN";
               }
            }else{
                 return "INCORRECT_TOKEN";
            }
        }else{
            return null;
        }
        
    }
    
    public String tokenGenerator(){
        String token = null;
        int longitud = 6;
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder resultado = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < longitud; i++) {
            int index = random.nextInt(letras.length());
            resultado.append(letras.charAt(index));
        }
            token = resultado.toString();
            return token;
    }
    
    public byte[] convertImageToBlob(MultipartFile file) throws IOException {
        return file.getBytes();
    }

    public String addSaldo(double saldoAfeixir, String email){
        try{
            User user = getUser(email);
            if(user == null){
                return "USER_NOT_FOUND";
            }else{
                user.setSaldoDisponible(user.getSaldoDisponible() + saldoAfeixir);
                userRepo.save(user);
                return "SALDO_ADDED";
            }
        }catch(Exception e){
            return e.getMessage();
        }
    }
    
    public String substractSaldo(double saldoSubstraer, String email){
        try{
            User user = getUser(email);
            if(user == null){
                return "USER_NOT_FOUND";
            }else{
                if(user.getSaldoDisponible() - saldoSubstraer < 0){
                    user.setSaldoDisponible(0);
                    userRepo.save(user);
                    return "SALDO_SUBSTRACTED";
                }else{
                    user.setSaldoDisponible(user.getSaldoDisponible() - saldoSubstraer);
                    userRepo.save(user);
                    return "SALDO_SUBSTRACTED";
                }
                
            }
        }catch(Exception e){
            return e.getMessage();
        }
    }    
        
        
        
        
        
        
        
}

