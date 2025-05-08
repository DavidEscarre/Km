/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.logica;

import cat.copernic.Entity.Recompensa;
import cat.copernic.controllers.API.UserApiController;
import cat.copernic.repository.RecompensaRepo;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author alpep
 */
@Service
public class RecompensaLogic {
     
    @Autowired
    private RecompensaRepo recompensaRepo;
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
    
    public List<Recompensa> findAllRecompenses() throws Exception{
        List<Recompensa> ret = new ArrayList<>();
        
        ret = recompensaRepo.findAll();
        
        return ret;
        
    }
    
    public Recompensa getRecompensa(Long id)throws Exception{
        
        Recompensa ret = recompensaRepo.findById(id).orElse(null);
        
        return ret;
    }
    public boolean existsById(Long id)throws Exception
    {
        Recompensa r = recompensaRepo.findById(id).orElse(null);
        
        return (r != null);
    }    
    
    public Long createRecompensa(Recompensa recompensa) {
        
        try {
           // user.setWord(passwordEncoder.encode(user.getWord()));
            Recompensa savedRecompensa = recompensaRepo.save(recompensa);
            return savedRecompensa.getId();
        } catch (Exception e) {
         
            return null;
        }

    }
     public List<Recompensa> getAllByCiclistaEmail(String ciclistaEmail) throws Exception{
        List<Recompensa> ret = new ArrayList<>();
        
        ret = recompensaRepo.findAllByCiclista_Email(ciclistaEmail);
        
        return ret;
        
    }
    
}
