/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import cat.copernic.Entity.Recompensa;
import cat.copernic.Entity.Sistema;
import cat.copernic.logica.SistemaLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author alpep
 */
   

@Controller
@RequestMapping("/")
public class SistemaWebController {
    @Autowired
    private SistemaLogic sistemaLogic;  
    
    
    @GetMapping("/")
    public String homePage(Model model, Authentication authentication) {
        double velMax;
        double puntsKm;
        double tempsMaxAtur;
        double tempsMaxRec;
        double precicioPunts;
        Sistema sistema = new Sistema();
        try{
           sistema = sistemaLogic.findAllSistemas().getFirst();
                   
                    
            velMax = sistema.getVelMaxValida();
            puntsKm = sistema.getPuntsKm();
            tempsMaxAtur = sistema.getTempsMaxAtur();
            tempsMaxRec = sistema.getTempsMaxRec();
            precicioPunts = sistema.getPrecisioPunts();
            
        }catch(Exception e){
            
            velMax =  60.00;
            puntsKm = 1.00;
            tempsMaxAtur = 300000L;
            tempsMaxRec = 259200000L;
            precicioPunts = 2000L;
               
        }
            model.addAttribute("sistema", sistema);
            model.addAttribute("velMax", velMax);
            model.addAttribute("puntsKm", puntsKm);
            model.addAttribute("tempsMaxAtur", tempsMaxAtur / 60000); // Min
            model.addAttribute("tempsMaxRec", tempsMaxRec / 3600000); // Horas
            model.addAttribute("precicioPunts",precicioPunts / 1000); // Seg
        
        return "index";
    }
    
    @PostMapping("/update")
    public String updateSistema(@ModelAttribute Sistema sistema, Model model, Authentication authentication, @RequestParam String action) {
        
        try {
           
            if (sistema == null) {
                model.addAttribute("errorMessage", "Camps incorrectes");
                return "redirect:/";
            }
            if ("guardar".equals(action)) {
                sistema.setTempsMaxAturMin(sistema.getTempsMaxAturMin());  // Min
                sistema.setTempsMaxRecHoras(sistema.getTempsMaxRecHoras()); // Horas
                sistema.setPrecicioPuntsSeg(sistema.getPrecicioPuntsSeg()); // Seg

                sistema.setId(sistemaLogic.findAllSistemas().getFirst().getId());
                sistemaLogic.updateSistema(sistema);

            }else if ("reset".equals(action)) {
                sistema.setTempsMaxAtur(300000L);  
                sistema.setTempsMaxRec(259200000L); 
                sistema.setPrecisioPunts(2000L); 
                sistema.setPuntsKm(1.00);
                sistema.setVelMaxValida(60.00);

                sistema.setId(sistemaLogic.findAllSistemas().getFirst().getId());
                sistemaLogic.updateSistema(sistema);
            }

            return "redirect:/";
            
        }catch(Exception e){
            return "index";
        }
            
    }
    /*
    @PostMapping("/reset")
    public String resetSistema(@ModelAttribute Sistema sistema, Model model, Authentication authentication) {
        try {
            
           if (sistema == null) {
                model.addAttribute("errorMessage", "Camps incorrectes");
                return "redirect:/";
            }
            sistema.setTempsMaxAtur(300000L);  
            sistema.setTempsMaxRec(259200000L); 
            sistema.setPrecisioPunts(2000L); 
            sistema.setPuntsKm(1.00);
            sistema.setVelMaxValida(60.00);
            
            sistema.setId(sistemaLogic.findAllSistemas().getFirst().getId());
            sistemaLogic.updateSistema(sistema);
           
            
                
            return "redirect:/";
            
        }catch(Exception e){
            return "index";
        }
            
    }*/
}
