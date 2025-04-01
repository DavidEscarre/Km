package com.example.km.core.models;

import com.example.km.core.utils.enums.Rol;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {

    /**
     * Identificador unic per al User. es el email sera la primary key
     */

    private String email;


    private String nom;


    private String observacions;


    private Rol rol;


    private boolean estat = true;


    private double saldoDisponible = 0.00;


    private LocalDateTime dataAlta;

    private String word;


    private byte[] foto;


    private int telefon;


    private String adreca;

    private List<Recompensa> recompenses = new ArrayList<>();
    private List<Ruta> rutes = new ArrayList<>();
    //constructors


    public User() {
    }

    public User(String email, String nom, String observacions, Rol rol, boolean estat, double saldoDisponible, LocalDateTime dataAlta, String word, byte[] foto, int telefon, String adreca, List<Ruta> rutes, List<Recompensa> recompenses) {
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
        this.rutes = rutes;
        this.recompenses = recompenses;
    }

    public List<Recompensa> getRecompenses() {
        return recompenses;
    }

    public void setRecompenses(List<Recompensa> recompenses) {
        this.recompenses = recompenses;
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


}
