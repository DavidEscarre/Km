/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.PuntGPS;
import cat.copernic.logica.PuntGPSLogic;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alpep
 */
@RestController
@RequestMapping("/rest/puntgps")
public class PuntGPSApiController {
    @Autowired
    private PuntGPSLogic puntGPSLogic;
    
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
        
    @GetMapping("/all")
    public ResponseEntity<List<PuntGPS>> findAll(){
        
        List<PuntGPS> llista;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");
        
        try{
            
            llista = puntGPSLogic.findAllPuntsGPS();
            logger.info("Trobada llista de tots els puntsGPS", "trobades: "+llista.size());
            return new ResponseEntity<>(llista, headers, HttpStatus.OK);
            
        }catch(Exception e){
            logger.error("Llista de tots els puntsGPS no trobada, error del servidor", "Error message: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
    }
    
    @GetMapping("byId/{puntGPSId}")
    public ResponseEntity<PuntGPS> getPuntGPSById(@PathVariable Long puntGPSId){
        PuntGPS puntGPS;
        
        ResponseEntity<PuntGPS> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        
        try {
            
            logger.info("Cercant puntGPS amb id: {}", "puntGPSId: "+puntGPSId);
            puntGPS = puntGPSLogic.getPuntGPS(puntGPSId);
            
            if (puntGPS == null)
            {
                logger.info("PuntGPS no trobat: {}", "puntGPSId: "+puntGPSId); 
                response = new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }
            else
            {
                logger.info("PuntGPS trobat: {}", "puntGPSId: "+puntGPSId); 
                response = new ResponseEntity<>(puntGPS, headers, HttpStatus.OK);
            }
            
        } catch (Exception e) {
            logger.error("Error intern del servidor al intentar trovar el puntGPS {}", "Error mesage: "+e.getMessage()); 
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;        
    }
}
