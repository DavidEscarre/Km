/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.Recompensa;
import cat.copernic.enums.EstatRecompensa;
import cat.copernic.logica.RecompensaLogic;
import cat.copernic.logica.UserLogic;
import jakarta.annotation.security.PermitAll;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alpep
 */
@RestController
@RequestMapping("/rest/recompenses")
@CrossOrigin(origins = "*")  // Permitir acceso desde cualquier origen
public class RecompensaApiController {
    
    @Autowired
    private RecompensaLogic recompensaLogic;
    @Autowired
    private UserLogic userLogic;
    
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
    
    
    @GetMapping("/byCiclistaEmail/{ciclistaEmail}")
    @PermitAll  // Permitir acceso sin autenticación
    public ResponseEntity<List<Recompensa>> getAllByCiclistaEmail(@PathVariable String ciclistaEmail){
        
        ResponseEntity<List<Recompensa>> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        
        try {
            logger.info("Cercant recompenses amb idCiclista: {}", "ciclistaId: "+ciclistaEmail);
            if(!userLogic.existsByEmail(ciclistaEmail)){
                logger.info("No s'ha trobat cap ciclista amb idCiclista: {}", "ciclistaId: "+ciclistaEmail);
                return new ResponseEntity<>( HttpStatus.NOT_FOUND);
            }else{
                List<Recompensa> recompensas = recompensaLogic.getAllByCiclistaEmail(ciclistaEmail);
                
                logger.info("Rutas trobades: {}", "ciclistaEmail: "+ciclistaEmail); 
                return new ResponseEntity<>(recompensas, headers, HttpStatus.OK);
            } 
        } catch (Exception e) {
            logger.error("Error intern del servidor al intentar trovar la ruta {}", "Error mesage: "+e.getMessage()); 
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }  
    }
    
    @PostMapping(value = "/reservar/{recompensaId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> reservarRecompensa(@PathVariable Long recompensaId, @RequestParam String email){
          
        ResponseEntity<String> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        headers.setContentType(MediaType.TEXT_PLAIN);
        
        try {
            if(!recompensaLogic.existsById(recompensaId)){
             
                return new ResponseEntity<>("Recompensa no trobada", headers, HttpStatus.NOT_FOUND);
            }else if(recompensaLogic.getRecompensa(recompensaId).getEstat()==EstatRecompensa.DISPONIBLE){
                
                String res = recompensaLogic.reservarRecompensa(recompensaId, email);
                if(res.equals("USER_YA_TIENE")){
                    return new ResponseEntity<>("L'usuari te reserves o assignacions actives.", headers, HttpStatus.FORBIDDEN);
                }else if(res.equals("USER_SALDO_INSUFICIENT")){
                    return new ResponseEntity<>( "Saldo insuficient.", headers, HttpStatus.FORBIDDEN);
                }else if(res.equals(recompensaId.toString())){
                    Recompensa recompensa = recompensaLogic.getRecompensa(recompensaId);
                    return new ResponseEntity<>(recompensa.getId().toString(), headers, HttpStatus.OK);
                }else{
                    return new ResponseEntity<>( "Recompensa no trobada.", headers,HttpStatus.NOT_FOUND);
                }
               
            }else{
                return new ResponseEntity<>("Recompensa no disponible.", headers, HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            logger.error("Error intern del servidor al intentar reservar la recompensa", "Error mesage: "+e.getMessage()); 
            return new ResponseEntity<>("Error intern del servidor.", headers,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(value = "/anularReserva/{recompensaId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> anularReservaRecompensa(@PathVariable Long recompensaId, @RequestParam String email){
          
        ResponseEntity<Recompensa> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        headers.setContentType(MediaType.TEXT_PLAIN);
        try {
            if(!recompensaLogic.existsById(recompensaId)){
             
                return new ResponseEntity<>( "Recompensa no trobada.",HttpStatus.NOT_FOUND);
            }else if(recompensaLogic.getRecompensa(recompensaId).getEstat()==EstatRecompensa.RESERVADA){
                String res = recompensaLogic.anularReservaRecompensa(recompensaId, email);
                if(res.equals(recompensaId.toString())){
                    Recompensa recompensa = recompensaLogic.getRecompensa(recompensaId);
                    return new ResponseEntity<>(recompensa.getId().toString(), headers, HttpStatus.OK);
                }else{
                    return new ResponseEntity<>("Error de la recompensa." ,HttpStatus.FORBIDDEN);
                }
            }else{
                return new ResponseEntity<>("Recompensa no reservada.", HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            logger.error("Error intern del servidor al intentar reservar la recompensa", "Error mesage: "+e.getMessage()); 
            return new ResponseEntity<>("Error intern del servidor.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/recollir/{recompensaId}")
    public ResponseEntity<String> recollirRecompensa(@PathVariable Long recompensaId, @RequestParam String email){
        Recompensa recompensa;
        
        ResponseEntity<String> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        
        try {
            
            logger.info("Cercant recompensa amb id: {}", "recompensaId: "+recompensaId);
            recompensa = recompensaLogic.getRecompensa(recompensaId);
            
            if (recompensa == null){
            
                logger.info("Recompensa no trobada: {}", "recompensaId: "+recompensaId); 
                return new ResponseEntity<>(headers, HttpStatus.NOT_FOUND);
            }
            else if (recompensa.getEstat().equals(EstatRecompensa.ASSIGNADA)){
               String res = recompensaLogic.recollirRecompensa(recompensaId,email);
               if(res.equals("USER_NOT_FOUND")){
                    return new ResponseEntity<>("Ciclista de la recompensa no trobat.", headers, HttpStatus.NOT_FOUND);
               }else if(res.equals("RECOMPENSA_NOT_FOUND")){
                    return new ResponseEntity<>("Recompensa no trobada.", headers, HttpStatus.NOT_FOUND);
               }else if(res.equals(recompensaId.toString())){
                   
                        return new ResponseEntity<>(recompensa.getId().toString(), headers, HttpStatus.OK);

               }else{
                    return new ResponseEntity<>("Error intern del servidor.", headers, HttpStatus.FORBIDDEN);
               }
            }else{
                
                return new ResponseEntity<>("Recompensa no assignada.", headers, HttpStatus.FORBIDDEN);
            }
            
        } catch (Exception e) {
            logger.error("Error intern del servidor al intentar trovar la recompensa {}", "Error mesage: "+e.getMessage()); 
            return new ResponseEntity<>("Error intern del servidor.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
              
    }
    
}
