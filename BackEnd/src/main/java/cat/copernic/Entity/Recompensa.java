/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cat.copernic.Entity;

import cat.copernic.enums.EstatRecompensa;
import cat.copernic.enums.EstatRuta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
/**
 *
 * @author alpep
 */
@Entity
@Table(name = "Recompensa")
public class Recompensa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcio;

    private String observacions;

    @Column(nullable = false)
    private double preu;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstatRecompensa estat = EstatRecompensa.DISPONIBLE;

    @Column(nullable = false)
    private LocalDateTime dataCreacio;

    private LocalDateTime dataAssignacio;
    
    private LocalDateTime dataRecollida;
    
    @ManyToOne
    @JoinColumn(name = "email_ciclista")
    private User ciclista;

    @Column(nullable = false, length = 255)
    private String puntBescanvi;

    public Recompensa() {
    }

    public Recompensa(String descripcio, String observacions, double preu, LocalDateTime dataCreacio, LocalDateTime dataAssignacio, LocalDateTime dataRecollida, User ciclista, String puntBescanvi) {
        this.descripcio = descripcio;
        this.observacions = observacions;
        this.preu = preu;
        this.dataCreacio = dataCreacio;
        this.dataAssignacio = dataAssignacio;
        this.dataRecollida = dataRecollida;
        this.ciclista = ciclista;
        this.puntBescanvi = puntBescanvi;
    }

    public Recompensa(Long id, String descripcio, String observacions, double preu, LocalDateTime dataCreacio, LocalDateTime dataAssignacio, LocalDateTime dataRecollida, User ciclista, String puntBescanvi) {
        this.id = id;
        this.descripcio = descripcio;
        this.observacions = observacions;
        this.preu = preu;
        this.dataCreacio = dataCreacio;
        this.dataAssignacio = dataAssignacio;
        this.dataRecollida = dataRecollida;
        this.ciclista = ciclista;
        this.puntBescanvi = puntBescanvi;
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

    public double getPreu() {
        return preu;
    }

    public void setPreu(double preu) {
        this.preu = preu;
    }

    public EstatRecompensa getEstat() {
        return estat;
    }

    public void setEstat(EstatRecompensa estat) {
        this.estat = estat;
    }

    public LocalDateTime getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(LocalDateTime dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public LocalDateTime getDataAssignacio() {
        return dataAssignacio;
    }

    public void setDataAssignacio(LocalDateTime dataAssignacio) {
        this.dataAssignacio = dataAssignacio;
    }

    public LocalDateTime getDataRecollida() {
        return dataRecollida;
    }

    public void setDataRecollida(LocalDateTime dataRecollida) {
        this.dataRecollida = dataRecollida;
    }

    public User getCiclista() {
        return ciclista;
    }

    public void setCiclista(User ciclista) {
        this.ciclista = ciclista;
    }

    public String getPuntBescanvi() {
        return puntBescanvi;
    }

    public void setPuntBescanvi(String puntBescanvi) {
        this.puntBescanvi = puntBescanvi;
    }
    
    
    
    
    
    

}
