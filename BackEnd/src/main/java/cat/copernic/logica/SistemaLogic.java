/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.logica;

import cat.copernic.Entity.Sistema;
import cat.copernic.repository.SistemaRepo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author alpep
 */
@Service
public class SistemaLogic {
    @Autowired
    private SistemaRepo sistemaRepo;
    
    public List<Sistema> findAllSistemas() throws Exception{
        List<Sistema> ret = new ArrayList<>();
        
        ret = sistemaRepo.findAll();
        
        return ret;
        
    }
    
    public Sistema getSistema(Long id)throws Exception{
        
        Sistema ret = sistemaRepo.findById(id).orElse(null);
        
        return ret;
    }
    public boolean existsById(Long id)throws Exception
    {
        Sistema s = sistemaRepo.findById(id).orElse(null);
        
        return (s != null);
    }    
    
    
}
