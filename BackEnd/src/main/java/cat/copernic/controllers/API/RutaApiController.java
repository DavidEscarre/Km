/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.Ruta;
import cat.copernic.logica.RutaLogic;
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
@RequestMapping("/rest/rutes")
public class RutaApiController {
    @Autowired
    private RutaLogic rutaLogic;
    
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
        
    @GetMapping("/all")
    public ResponseEntity<List<Ruta>> findAll(){
        
        List<Ruta> llista;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store");
        
        try{
            
            llista = rutaLogic.findAllRutes();
            logger.info("Trobada llista de totes les rutes", "trobades: "+llista.size());
            return new ResponseEntity<>(llista, headers, HttpStatus.OK);
            
        }catch(Exception e){
            logger.error("Llista de totes les rutes no trobada, error del servidor", "Error message: "+e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
    }
    
    @GetMapping("byId/{rutaId}")
    public ResponseEntity<Ruta> getRutaById(@PathVariable Long rutaId){
        Ruta ruta;
        
        ResponseEntity<Ruta> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        
        try {
            
            logger.info("Cercant ruta amb id: {}", "rutaId: "+rutaId);
            ruta = rutaLogic.getRuta(rutaId);
            
            if (ruta == null)
            {
                logger.info("Ruta no trobada: {}", "rutaId: "+rutaId); 
                response = new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }
            else
            {
                logger.info("Ruta trobada: {}", "rutaId: "+rutaId); 
                response = new ResponseEntity<>(ruta, headers, HttpStatus.OK);
            }
            
        } catch (Exception e) {
            logger.error("Error intern del servidor al intentar trovar la ruta {}", "Error mesage: "+e.getMessage()); 
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;        
    }
}
