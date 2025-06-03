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
@Table(name = "Exemple")
public class Exemple {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcio;

    private String observacions;

    //@Column(nullable = false)
  //  private double preu;

    public Exemple() {
    }

    public Exemple(Long id, String descripcio, String observacions) {
        this.id = id;
        this.descripcio = descripcio;
        this.observacions = observacions;
       // this.preu = preu;
    }

    public Exemple(String descripcio, String observacions) {
        this.descripcio = descripcio;
        this.observacions = observacions;
       // this.preu = preu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public String getObservacions() {
        return observacions;
    }

    public void setObservacions(String observacions) {
        this.observacions = observacions;
    }

  /*  public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }*/
    
    
    
    
}
