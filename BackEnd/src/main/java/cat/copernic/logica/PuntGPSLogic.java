/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.logica;

import cat.copernic.Entity.PuntGPS;
import cat.copernic.controllers.API.UserApiController;
import cat.copernic.repository.PuntGPSRepo;
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
public class PuntGPSLogic {
    @Autowired
    private PuntGPSRepo puntGPSRepo;
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
    
    public List<PuntGPS> findAllPuntsGPS() throws Exception{
        List<PuntGPS> ret = new ArrayList<>();
        
        ret = puntGPSRepo.findAll();
        
        return ret;
        
    }
    
    public PuntGPS getPuntGPS(Long id)throws Exception{
        
        PuntGPS ret = puntGPSRepo.findById(id).orElse(null);
        
        return ret;
    }
    public boolean existsById(Long id)throws Exception
    {
        PuntGPS p = puntGPSRepo.findById(id).orElse(null);
        
        return (p != null);
    }    
}
