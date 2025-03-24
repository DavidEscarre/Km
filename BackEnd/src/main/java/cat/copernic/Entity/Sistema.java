/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cat.copernic.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author alpep
 */
@Entity
@Table(name = "Sistema")
public class Sistema {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double velMaxValida = 60.00;

    @Column(nullable = false)
    private Long tempsMaxAtur = 300000L;

    @Column(nullable = false)
    private double puntsKm = 1.00;

    @Column(nullable = false)
    private Long tempsMaxRec = 259200000L;

    
    public Sistema(Long id) {
        this.id = id;
    }

    public Sistema() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getVelMaxValida() {
        return velMaxValida;
    }

    public void setVelMaxValida(double velMaxValida) {
        this.velMaxValida = velMaxValida;
    }

    public Long getTempsMaxAtur() {
        return tempsMaxAtur;
    }

    public void setTempsMaxAtur(Long tempsMaxAtur) {
        this.tempsMaxAtur = tempsMaxAtur;
    }

    public double getPuntsKm() {
        return puntsKm;
    }

    public void setPuntsKm(double puntsKm) {
        this.puntsKm = puntsKm;
    }

    public Long getTempsMaxRec() {
        return tempsMaxRec;
    }

    public void setTempsMaxRec(Long tempsMaxRec) {
        this.tempsMaxRec = tempsMaxRec;
    }
    
    

}
