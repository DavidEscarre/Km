/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.API;


import cat.copernic.Entity.Exemple;
import cat.copernic.repository.ExempleRepo;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author alpep
 */
@RestController
@RequestMapping("/rest/exemple")
public class ExempleApiController {
    
   @Autowired
    private ExempleRepo exempleRepo;
    
    Logger logger = LoggerFactory.getLogger(ExempleApiController.class);
    
    
    @PostConstruct
    private void init() {
        logger.info("✅ ExempleApiController initialized");
    }
    
      @PostMapping("/create")
    public ResponseEntity<Exemple> createRuta(@RequestBody Exemple ex) {
          logger.info("✅ Exemple recivido");
        try {
            if (ex == null) {
                logger.error("❌ Exemple received is null");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
        
        
       
            Exemple Savedex = exempleRepo.save(ex);
            logger.info("✅ Exemple created with ID: {}", Savedex.getId());
             return ResponseEntity.ok(Savedex);
        } catch (Exception e) {
            logger.error("❌ Error creating Exemple", e);
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
}