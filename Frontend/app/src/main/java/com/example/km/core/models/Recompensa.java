package com.example.km.core.models;

import com.example.km.core.utils.enums.EstatRecompensa;

import java.time.LocalDateTime;

public class Recompensa {

    private Long id;


    private String descripcio;

    private String observacions;


    private double preu;


    private EstatRecompensa estat = EstatRecompensa.DISPONIBLE;


    private LocalDateTime dataCreacio;

    private LocalDateTime dataAssignacio;
    private LocalDateTime dataRecollida;


    private User ciclista;


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
