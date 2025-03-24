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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 *
 * @author alpep
 */
@Entity
@Table(name = "PuntGPS")
public class PuntGPS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_ruta", nullable = false)
    private Ruta ruta;

    @Column(nullable = false)
    private long latitud;

    @Column(nullable = false)
    private long longitud;

    @Column(nullable = false)
    private LocalDateTime marcaTemps;

    public PuntGPS() {
    }

    public PuntGPS(Ruta ruta, long latitud, long longitud, LocalDateTime marcaTemps) {
        this.ruta = ruta;
        this.latitud = latitud;
        this.longitud = longitud;
        this.marcaTemps = marcaTemps;
    }

    public PuntGPS(Long id, Ruta ruta, long latitud, long longitud, LocalDateTime marcaTemps) {
        this.id = id;
        this.ruta = ruta;
        this.latitud = latitud;
        this.longitud = longitud;
        this.marcaTemps = marcaTemps;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public long getLatitud() {
        return latitud;
    }

    public void setLatitud(long latitud) {
        this.latitud = latitud;
    }

    public long getLongitud() {
        return longitud;
    }

    public void setLongitud(long longitud) {
        this.longitud = longitud;
    }

    public LocalDateTime getMarcaTemps() {
        return marcaTemps;
    }

    public void setMarcaTemps(LocalDateTime marcaTemps) {
        this.marcaTemps = marcaTemps;
    }
    
    
    
    
    
    
    
}

