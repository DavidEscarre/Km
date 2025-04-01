/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package cat.copernic.Entity;

import cat.copernic.enums.Rol;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author alpep
 */

@Entity
@Table(name = "Usuari")
public class User implements UserDetails{

    /**
     * Identificador unic per al User. es el email sera la primary key
     */
    @Id
    @Column(nullable= false, name = "email")
    private String email;

    @Column(nullable = false,name = "nom_complet")
    private String nom;
    
    @Column(nullable = true)
    private String observacions;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;
    
    @Column(nullable = false)
    private boolean estat = true;
    
    @Column(nullable = true, name = "saldo_disponible")
    private double saldoDisponible = 0.00;
    
    @CreationTimestamp
    @Column(nullable = false, name = "data_alta")
    private LocalDateTime dataAlta;
    
    @Column(nullable = false)
    private String word;
    
    @Lob
    @Column(nullable = true, columnDefinition = "LONGBLOB")
    private byte[] foto;
    
    @Column(nullable = true, length = 9)
    private int telefon;
    
    @Column(nullable = true)
    private String adreca;
    
    @OneToMany(mappedBy = "ciclista", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Recompensa> recompensas = new ArrayList<>();
    
    @OneToMany(mappedBy = "ciclista", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonIgnore
    private List<Ruta> rutes = new ArrayList<>();
   
    //constructors
    
    public User() {
        
    }

    public User(String email, String nom, String observacions, Rol rol, boolean estat, double saldoDisponible,
            LocalDateTime dataAlta, String word, byte[] foto, int telefon, String adreca, List<Recompensa> recompensas, List<Ruta> rutes) {
        this.email = email;
        this.nom = nom;
        this.observacions = observacions;
        this.rol = rol;
        this.estat = estat;
        this.saldoDisponible = saldoDisponible;
        this.dataAlta = dataAlta;
        this.word = word;
        this.foto = foto;
        this.telefon = telefon;
        this.adreca = adreca;
        this.recompensas = recompensas;
        this.rutes = rutes;
    }

    public List<Recompensa> getRecompensas() {
        return recompensas;
    }

    public void setRecompensas(List<Recompensa> recompensas) {
        this.recompensas = recompensas;
    }

    public List<Ruta> getRutes() {
        return rutes;
    }

    public void setRutes(List<Ruta> rutes) {
        this.rutes = rutes;
    }

    
    public String getEmail() {
        return email;
    }
    

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getObservacions() {
        return observacions;
    }

    public void setObservacions(String observacions) {
        this.observacions = observacions;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public boolean isEstat() {
        return estat;
    }

    public void setEstat(boolean estat) {
        this.estat = estat;
    }

    public double getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(double saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public LocalDateTime getDataAlta() {
        return dataAlta;
    }

    public void setDataAlta(LocalDateTime dataAlta) {
        this.dataAlta = dataAlta;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public int getTelefon() {
        return telefon;
    }

    public void setTelefon(int telefon) {
        this.telefon = telefon;
    }

    public String getAdreca() {
        return adreca;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

   
      @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        List<GrantedAuthority> ret = new ArrayList<>();
        
        String[] llista1 = {"CICLISTA","ADMIN"};
        
       
        
        return ret;
    }
    
    
    
    @Override
    public String getPassword() {
       return this.word;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    
       
    
    

}
