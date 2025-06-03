/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.config;

import cat.copernic.Entity.User;
import cat.copernic.enums.Rol;
import cat.copernic.repository.UserRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio para validar usuarios basado únicamente en clientes.
 * 
 * @author david
 */
@Service
public class ValidadorUsuaris implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar clientes por correo electrónico
        User usuari = userRepo.findById(username).orElse(null);

        if (usuari == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + username);
        }else{
            if(usuari.getRol().equals(Rol.CICLISTA)){
                throw new UsernameNotFoundException("Usuari no es un Admin: " + username);
            }

        }
        

        // Retornar el cliente como un objeto UserDetails
        return usuari; // Asegúrate de que la clase Client implemente UserDetails.
    }
}
