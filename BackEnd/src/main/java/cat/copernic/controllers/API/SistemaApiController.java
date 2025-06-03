/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.Sistema;
import cat.copernic.logica.SistemaLogic;
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
@RequestMapping("/rest/sistema")
public class SistemaApiController {
    @Autowired
    private SistemaLogic sistemaLogic;
    
    Logger logger = LoggerFactory.getLogger(SistemaApiController.class);
        
    @GetMapping("/all")
    public ResponseEntity<List<Sistema>> findAll(){
        
        List<Sistema> llista;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");
        
        try{
            
            llista = sistemaLogic.findAllSistemas();
            logger.info("Trobada llista de tots els sistemes", "trobats: "+llista.size());
            return new ResponseEntity<>(llista, headers, HttpStatus.OK);
            
        }catch(Exception e){
            logger.error("Llista de tots els sistemes no trobada, error del servidor", "Error message: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
    }
    
    @GetMapping("byId/{sistemaId}")
    public ResponseEntity<Sistema> getSistemaById(@PathVariable Long sistemaId){
        Sistema sistema;
        
        ResponseEntity<Sistema> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        

        try {
            logger.info("Cercant sistema amb id: {}", "sistemaId: "+sistemaId);
            sistema = sistemaLogic.getSistema(sistemaId);
                 
            
            if (sistema == null)
            {
                 logger.info("Sistema no trobat: {}", "sistemaId: "+sistemaId); 
                response = new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }
            else
            {
                logger.info("Sistema trobat: {}", "sistemaId: "+sistemaId); 
                response = new ResponseEntity<>(sistema, headers, HttpStatus.OK);
            }
            
        } catch (Exception e) {
            logger.error("Error intern del servidor al intentar trovar el sistema {}", "Error mesage: "+e.getMessage()); 
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;        
    }
 
    
    
}
