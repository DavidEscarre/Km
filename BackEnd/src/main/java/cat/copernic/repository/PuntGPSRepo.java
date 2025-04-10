/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package cat.copernic.repository;

import cat.copernic.Entity.PuntGPS;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author alpep
 */
@Repository
public interface PuntGPSRepo extends JpaRepository<PuntGPS, Long> {   
    
     List<PuntGPS> findByRuta_Id(Long id);
}
