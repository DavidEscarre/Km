/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cat.copernic.Entity;

import cat.copernic.enums.EstatRuta;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author alpep
 */
@Entity
@Table(name = "Ruta")
public class Ruta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "email_ciclista", nullable = false)
    private User ciclista;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstatRuta estat = EstatRuta.PENDENT;

    @Column(nullable = false)
    private LocalDateTime dataInici;

    private LocalDateTime dataFinal;

    @Column(nullable = false)
    private double saldo = 0.00;

    @Column(nullable = false)
    private double distancia = 0.00;

    @Column(nullable = false)
    private double velocitatMitjana = 0.00;

    @Column(nullable = false)
    private double velocitatMax = 0.00;

    
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<PuntGPS> puntsGPS = new ArrayList<>();
    
    
    public Ruta() {
    }

    public Ruta(Long id, User ciclista, LocalDateTime dataInici, LocalDateTime dataFinal, List<PuntGPS> puntsGPS) {
        this.id = id;
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;
        this.puntsGPS = puntsGPS;
    }

    public Ruta(User ciclista, LocalDateTime dataInici, LocalDateTime dataFinal) {
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;
    }
    public Ruta(Long id, User ciclista, LocalDateTime dataInici, LocalDateTime dataFinal) {
        this.id = id;
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;
    }

    public Ruta(User ciclista, LocalDateTime dataInici, LocalDateTime dataFinal, List<PuntGPS> puntsGPS) {
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;
        this.puntsGPS = puntsGPS;
    }

    public List<PuntGPS> getPuntsGPS() {
        return puntsGPS;
    }

    public void setPuntsGPS(List<PuntGPS> puntsGPS) {
        this.puntsGPS = puntsGPS;
    }

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getCiclista() {
        return ciclista;
    }

    public void setCiclista(User ciclista) {
        this.ciclista = ciclista;
    }

    public EstatRuta getEstat() {
        return estat;
    }

    public void setEstat(EstatRuta estat) {
        this.estat = estat;
    }

    public LocalDateTime getDataInici() {
        return dataInici;
    }

    public void setDataInici(LocalDateTime dataInici) {
        this.dataInici = dataInici;
    }

    public LocalDateTime getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(LocalDateTime dataFinal) {
        this.dataFinal = dataFinal;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public double getVelocitatMitjana() {
        return velocitatMitjana;
    }

    public void setVelocitatMitjana(double velocitatMitjana) {
        this.velocitatMitjana = velocitatMitjana;
    }

    public double getVelocitatMax() {
        return velocitatMax;
    }

    public void setVelocitatMax(double velocitatMax) {
        this.velocitatMax = velocitatMax;
    }
    
    
}
