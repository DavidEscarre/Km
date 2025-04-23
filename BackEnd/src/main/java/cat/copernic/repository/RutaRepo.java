/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cat.copernic.repository;

import cat.copernic.Entity.Ruta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author alpep
 */
@Repository
public interface RutaRepo extends JpaRepository<Ruta, Long> {  
    List<Ruta> findAllByCiclista_Email(String email);
}
