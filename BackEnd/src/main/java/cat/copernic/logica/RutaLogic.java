/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.logica;

import cat.copernic.Entity.PuntGPS;
import cat.copernic.Entity.Ruta;
import cat.copernic.controllers.API.UserApiController;
import cat.copernic.repository.RutaRepo;
import cat.copernic.repository.UserRepo;
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
public class RutaLogic {
    
    @Autowired
    private RutaRepo rutaRepo;
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
    
    public List<Ruta> findAllRutes() throws Exception{
        List<Ruta> ret = new ArrayList<>();
        
        ret = rutaRepo.findAll();
        
        return ret;
        
    }
    
    public Ruta getRuta(Long id)throws Exception{
        
        Ruta ret = rutaRepo.findById(id).orElse(null);
        
        return ret;
    }
    public boolean existsById(Long id)throws Exception
    {
        Ruta r = rutaRepo.findById(id).orElse(null);
        
        return (r != null);
    }    
    
    
    public Long saveRuta(Ruta ruta)throws Exception{

        Long rutaId = rutaRepo.save(ruta).getId();
            
        return rutaId;

    }
    public Long updateRuta(Ruta ruta)throws Exception{
       
        Ruta OldRuta = rutaRepo.findById(ruta.getId()).orElse(null);
        
        if(OldRuta==null){
            return null;
        }
        
       OldRuta.setCiclista(ruta.getCiclista());
       OldRuta.setDataFinal(ruta.getDataFinal());
       OldRuta.setDataInici(ruta.getDataInici());
       OldRuta.setDistancia(ruta.getDistancia());
       OldRuta.setEstat(ruta.getEstat());
       OldRuta.setSaldo(ruta.getSaldo());
       OldRuta.setVelocitatMax(ruta.getVelocitatMax());
       OldRuta.setVelocitatMitjana(ruta.getVelocitatMitjana());
       
       return rutaRepo.save(OldRuta).getId();
        
    }
}
