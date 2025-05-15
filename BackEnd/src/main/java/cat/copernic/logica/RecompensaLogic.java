/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.logica;

import cat.copernic.Entity.Recompensa;
import cat.copernic.Entity.User;
import cat.copernic.controllers.API.UserApiController;
import cat.copernic.enums.EstatRecompensa;
import cat.copernic.repository.RecompensaRepo;
import cat.copernic.repository.UserRepo;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author alpep
 */
@Service
public class RecompensaLogic {
     
    @Autowired
    private RecompensaRepo recompensaRepo;
    
    @Autowired
    private UserLogic UserLogic;
    
     
    @Autowired
    private UserRepo userRepo;
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
    
    public List<Recompensa> findAllRecompenses() throws Exception{
        List<Recompensa> ret = new ArrayList<>();
        
        ret = recompensaRepo.findAll();
        
        return ret;
        
    }
    public String reservarRecompensa(Long id, String email)throws Exception{
        
        Recompensa ret = recompensaRepo.findById(id).orElse(null);
        if(ret!=null){
             User user = userRepo.findById(email).orElse(null);
             if(user!=null && user.getSaldoDisponible() >= ret.getPreu()){
                 
                List<Recompensa> reservesRecUser = user.getRecompensas().stream()
                        .filter(recompensa -> (recompensa.getEstat().equals(EstatRecompensa.RESERVADA) 
                                || (recompensa.getEstat().equals(EstatRecompensa.ASSIGNADA))))
                        .collect(Collectors.toList());
                if(reservesRecUser.size()>=1){
                    return "USER_YA_TIENE";
                }
                ret.setEstat(EstatRecompensa.RESERVADA);
                ret.setDataReserva(LocalDateTime.now());
                ret.setCiclista(user);
             }else{
                 return "USER_SALDO_INSUFICIENT";
             }
             
            recompensaRepo.save(ret);
       
        
            return ret.getId().toString();
        }else{
            return null;
        }
        
    }
    public String anularReservaRecompensa(Long id, String email)throws Exception{
        
        Recompensa ret = recompensaRepo.findById(id).orElse(null);
        if(ret!=null){
            User user = userRepo.findById(email).orElse(null);
            if(user!=null && ret.getCiclista().equals(user)){
                ret.setEstat(EstatRecompensa.DISPONIBLE);
                ret.setDataReserva(null);
                ret.setCiclista(null);
            }else{
                return null;
            }
            recompensaRepo.save(ret);
 
            return ret.getId().toString();
        }else{
            return null;
        }
       
    }
    
    public String recollirRecompensa(Long id, String email)throws Exception{
        
        Recompensa ret = recompensaRepo.findById(id).orElse(null);
        if(ret!=null){
            User user = userRepo.findById(email).orElse(null);
            if(user!=null && ret.getCiclista().equals(user)){
                ret.setEstat(EstatRecompensa.RECOLLIDA);
                ret.setDataRecollida(LocalDateTime.now());
                recompensaRepo.save(ret);
                
                return ret.getId().toString();
            }else{
                return "USER_NOT_FOUND";
            }
            
 
           
        }else{
            return "RECOMPENSA_NOT_FOUND";
        }
       
    }
    
    public String assignarRecompensa(Long id)throws Exception{
        
        Recompensa ret = recompensaRepo.findById(id).orElse(null);
        
        if(ret!=null && ret.getEstat().equals(EstatRecompensa.RESERVADA)){
            User user = ret.getCiclista();
            if(user.getSaldoDisponible() >= ret.getPreu()){
                
                String res = UserLogic.substractSaldo(ret.getPreu(), user.getEmail());
               
                if(res.equals("USER_NOT_FOUND")){
                    return res;
                }else if (res.equals("SALDO_SUBSTRACTED")){
                    ret.setEstat(EstatRecompensa.ASSIGNADA);
                    ret.setDataAssignacio(LocalDateTime.now());
                                       
                    recompensaRepo.save(ret);
                    
                    return ret.getId().toString();
                }else{ return res; }
            }else{ return "USER_SALDO_INSUFICIENT"; }
        }else{
            return "RECOMPENSA_NO_RESERVADA";
        }
    }
    
    public Recompensa getRecompensa(Long id)throws Exception{
        
        Recompensa ret = recompensaRepo.findById(id).orElse(null);
        
        return ret;
    }
    public boolean existsById(Long id)throws Exception
    {
        Recompensa r = recompensaRepo.findById(id).orElse(null);
        
        return (r != null);
    }    
    
    public Long createRecompensa(Recompensa recompensa) {
        
        try {
           // user.setWord(passwordEncoder.encode(user.getWord()));
            Recompensa savedRecompensa = recompensaRepo.save(recompensa);
            return savedRecompensa.getId();
        } catch (Exception e) {
         
            return null;
        }

    }
     public List<Recompensa> getAllByCiclistaEmail(String ciclistaEmail) throws Exception{
        List<Recompensa> ret = new ArrayList<>();
        
        ret = recompensaRepo.findAllByCiclista_Email(ciclistaEmail);
        
        return ret;
        
    }
     
    public void deleteRecompensa(Long recompensaId) throws Exception{
        
        recompensaRepo.deleteById(recompensaId);

    }
    
}
