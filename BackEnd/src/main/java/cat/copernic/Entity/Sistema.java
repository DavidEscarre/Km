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

    @Column(nullable = false)
    private Long precisioPunts = 2000L;
  
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

    public Long getPrecisioPunts() {
        return precisioPunts;
    }

    public void setPrecisioPunts(Long precisioPunts) {
        this.precisioPunts = precisioPunts;
    }
    
      // Devuelve valor en minutos para mostrar
    public Long getTempsMaxAturMin() {
        return tempsMaxAtur != null ? tempsMaxAtur / 60000 : null;
    }

    public void setTempsMaxAturMin(Long minuts) {
        this.tempsMaxAtur = minuts != null ? minuts * 60000 : null;
    }
    
    public Long getTempsMaxRecHoras() {
        return tempsMaxRec != null ? tempsMaxRec / 3600000 : null;
    }

    public void setTempsMaxRecHoras(Long horas) {
        this.tempsMaxRec = horas != null ? horas * 3600000 : null;
    }
    
     public Long getPrecicioPuntsSeg() {
        return precisioPunts != null ? precisioPunts / 1000 : null;
    }

    public void setPrecicioPuntsSeg(Long segons) {
        this.precisioPunts = segons != null ? segons * 1000 : null;
    }
    
    

}
