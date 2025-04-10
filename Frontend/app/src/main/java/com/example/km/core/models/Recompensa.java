package com.example.km.core.models;

import com.example.km.core.utils.enums.EstatRecompensa;

import java.time.LocalDateTime;

public class Recompensa {

    private Long id;

    private String nom;

    private String descripcio;

    private String observacions;


    private double preu;


    private EstatRecompensa estat = EstatRecompensa.DISPONIBLE;


    private String dataCreacio;

    private String dataAssignacio;
    private String dataRecollida;


    private User ciclista;


    private String puntBescanvi;

    public Recompensa() {
    }

    public Recompensa(String nom, String descripcio, String observacions, double preu, String dataCreacio, String dataAssignacio, String dataRecollida, User ciclista, String puntBescanvi) {
        this.nom = nom;
        this.descripcio = descripcio;
        this.observacions = observacions;
        this.preu = preu;
        this.dataCreacio = dataCreacio;
        this.dataAssignacio = dataAssignacio;
        this.dataRecollida = dataRecollida;
        this.ciclista = ciclista;
        this.puntBescanvi = puntBescanvi;
    }

    public Recompensa(Long id, String nom, String descripcio, String observacions, double preu, String dataCreacio, String dataAssignacio, String dataRecollida, User ciclista, String puntBescanvi) {
        this.id = id;
        this.nom = nom;
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

    public String getDataCreacio() {
        return dataCreacio;
    }

    public void setDataCreacio(String dataCreacio) {
        this.dataCreacio = dataCreacio;
    }

    public String getDataAssignacio() {
        return dataAssignacio;
    }

    public void setDataAssignacio(String dataAssignacio) {
        this.dataAssignacio = dataAssignacio;
    }

    public String getDataRecollida() {
        return dataRecollida;
    }

    public void setDataRecollida(String dataRecollida) {
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


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
