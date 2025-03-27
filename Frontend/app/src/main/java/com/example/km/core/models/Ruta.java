package com.example.km.core.models;

import com.example.km.core.utils.enums.EstatRuta;

import java.time.LocalDateTime;

public class Ruta {
    private Long id;


    private User ciclista;


    private EstatRuta estat = EstatRuta.PENDENT;


    private String dataInici;

    private String dataFinal;


    private double saldo = 0.00;


    private double distancia = 0.00;


    private double velocitatMitjana = 0.00;


    private double velocitatMax = 0.00;

    public Ruta() {
    }

    public Ruta(Long id, User ciclista, String dataInici, String dataFinal) {
        this.id = id;
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;
    }

    public Ruta(User ciclista, String dataInici, String dataFinal) {
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;
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

    public String getDataInici() {
        return dataInici;
    }

    public void setDataInici(String dataInici) {
        this.dataInici = dataInici;
    }

    public String getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(String dataFinal) {
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