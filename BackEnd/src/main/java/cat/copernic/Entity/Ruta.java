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
import jakarta.persistence.Temporal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    
    @OneToMany(mappedBy = "ruta", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<PuntGPS> puntsGPS = new ArrayList<>();
    
    @Column(nullable = false)
    private Long tempsAturat = 0L;
    
    public Ruta() {
    }
    public Ruta(Long id) {
        this.id = id;
    }

    public Ruta(User ciclista, LocalDateTime dataInici) {
        this.ciclista = ciclista;
        this.dataInici = dataInici;
    }
    
    public Ruta(Long id, User ciclista, LocalDateTime dataInici, LocalDateTime dataFinal, List<PuntGPS> puntsGPS) {
        this.id = id;
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;
        this.puntsGPS = puntsGPS;
    }
    
    public Ruta(Long id, User ciclista, LocalDateTime dataInici, LocalDateTime dataFinal, List<PuntGPS> puntsGPS, Long tempsAturat) {
        this.id = id;
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;
        this.puntsGPS = puntsGPS;
        this.tempsAturat = tempsAturat;
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

    public Long getTempsAturat() {
        return tempsAturat;
    }

    public void setTempsAturat(Long tempsAturat) {
        this.tempsAturat = tempsAturat;
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
    
    
    public String getDataFormategada(LocalDateTime data) {
        if (data == null) {
            return " - ";
        }
        // Format “dd/MM/yyyy HH:mm”
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return data.format(fmt);
    }
    
    public String getDurada() {
        // Si falta cualquiera de las fechas, devolvemos un placeholder
        if (this.dataInici == null || this.dataFinal == null) {
            return " - ";
        }

        // Calculamos la duración entre dataInici y dataFinal
        Duration duration = Duration.between(this.dataInici, this.dataFinal);

        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        long seconds = duration
                .minusHours(hours)
                .minusMinutes(minutes)
                .getSeconds();

        // Formateamos a "HH:mm:ss"
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
