/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;

import cat.copernic.Entity.PuntGPS;
import cat.copernic.Entity.Ruta;
import cat.copernic.Entity.User;
import cat.copernic.logica.RutaLogic;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.PermitAll;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alpep
 */
@RestController
@RequestMapping("/rest/rutes")
@CrossOrigin(origins = "*")  // Permitir acceso desde cualquier origen
public class RutaApiController {
    @Autowired
    private RutaLogic rutaLogic;
    
    Logger logger = LoggerFactory.getLogger(RutaApiController.class);
        
    private ObjectMapper objectMapper;  // Para convertir JSON a objetos

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
    
    @GetMapping("/byId/{rutaId}")
    public ResponseEntity<Ruta> getRutaById(@PathVariable Long rutaId){
        
        ResponseEntity<Ruta> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        
        try {
            
            logger.info("Cercant ruta amb id: {}", "rutaId: "+rutaId);
            Ruta ruta = rutaLogic.getRuta(rutaId);
            
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
    
    @GetMapping("/byCiclistaEmail/{ciclistaEmail}")
    @PermitAll  // Permitir acceso sin autenticación
    public ResponseEntity<List<Ruta>> getAllByCiclistaEmail(@PathVariable String ciclistaEmail){
        
        ResponseEntity<List<Ruta>> response;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-store"); //no usar caché
        
        try {
            
            logger.info("Cercant rutes amb idCiclista: {}", "ciclistaId: "+ciclistaEmail);
           List<Ruta> rutas = rutaLogic.getAllByCiclistaEmail(ciclistaEmail);
            
            logger.info("Rutas trobades: {}", "ciclistaEmail: "+ciclistaEmail); 
            response = new ResponseEntity<>(rutas, headers, HttpStatus.OK);
           

        } catch (Exception e) {
            logger.error("Error intern del servidor al intentar trovar la ruta {}", "Error mesage: "+e.getMessage()); 
            response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return response;        
    }
    
      
   @PostMapping("/create")
   @PermitAll  // Permitir acceso sin autenticación
    public ResponseEntity<Long> createRuta(@RequestBody Ruta ruta) {
        try {
            if (ruta == null) {
                logger.error("❌ Ruta received is null");
                return ResponseEntity.badRequest().build();
            }

            Long rutaId = rutaLogic.saveRuta(ruta);
            logger.info("✅ Ruta created with ID: {}", rutaId);
            return new ResponseEntity<>(rutaId, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("❌ Error creating ruta", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping("/update")
    public ResponseEntity<Long> updateRuta(@RequestBody Ruta ruta) {
        try {
            if (ruta == null || ruta.getId() == null) {
                return ResponseEntity.badRequest().body(null);
            }

            Ruta OldRuta = rutaLogic.getRuta(ruta.getId());
            if (OldRuta == null) {
                return ResponseEntity.notFound().build();
            }

            Long rutaId = rutaLogic.updateRuta(ruta);
            if(rutaId != null){
                 return ResponseEntity.ok().body(ruta.getId());
            }else{
                 return ResponseEntity.notFound().build();
            }
           

        } catch (Exception e) {
            logger.error("❌ Error updating ad", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
}
    

