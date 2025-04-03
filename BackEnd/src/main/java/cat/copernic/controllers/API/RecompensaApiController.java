/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.Recompensa;
import cat.copernic.logica.RecompensaLogic;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alpep
 */
@RestController
@RequestMapping("/rest/recompenses")
public class RecompensaApiController {
    
    @Autowired
    private RecompensaLogic recompensaLogic;
    
    Logger logger = LoggerFactory.getLogger(RecompensaApiController.class);
        
    @GetMapping("/all")
    public ResponseEntity<List<Recompensa>> findAll(){
        
        List<Recompensa> llista;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");
        
        try{
            
            llista = recompensaLogic.findAllRecompenses();
            logger.info("Trobada llista de totes les recompenses", "trobades: "+llista.size());
            return new ResponseEntity<>(llista, headers, HttpStatus.OK);
            
        }catch(Exception e){
            logger.error("Llista de totes les recompenses no trobada, error del servidor", "Error message: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
    }
    
    @GetMapping("byId/{recompensaId}")
    public ResponseEntity<Recompensa> getRecompensaById(@PathVariable Long recompensaId){
        Recompensa recompensa;
        
        ResponseEntity<Recompensa> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        
        try {
            
            logger.info("Cercant recompensa amb id: {}", "recompensaId: "+recompensaId);
            recompensa = recompensaLogic.getRecompensa(recompensaId);
            
            if (recompensa == null)
            {
                logger.info("Recompensa no trobada: {}", "recompensaId: "+recompensaId); 
                response = new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }
            else
            {
                logger.info("Recompensa trobada: {}", "recompensaId: "+recompensaId); 
                response = new ResponseEntity<>(recompensa, headers, HttpStatus.OK);
            }
            
        } catch (Exception e) {
            logger.error("Error intern del servidor al intentar trovar la recompensa {}", "Error mesage: "+e.getMessage()); 
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;        
    }
}
