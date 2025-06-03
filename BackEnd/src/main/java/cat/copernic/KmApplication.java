/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package cat.copernic;

import cat.copernic.Entity.User;
import cat.copernic.logica.PuntGPSLogic;
import cat.copernic.repository.PuntGPSRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author alpep
 */
@SpringBootApplication
@ComponentScan
public class KmApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(KmApplication.class, args);
         
         
        
         
    }

}
