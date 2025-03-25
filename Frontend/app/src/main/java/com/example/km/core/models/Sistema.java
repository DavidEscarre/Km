package com.example.km.core.models;

public class Sistema {

    private Long id;

    private double velMaxValida = 60.00;

    private Long tempsMaxAtur = 300000L;

    private double puntsKm = 1.00;

    private Long tempsMaxRec = 259200000L;


    public Sistema(Long id) {
        this.id = id;
    }

    public Sistema() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getVelMaxValida() {
        return velMaxValida;
    }

    public void setVelMaxValida(double velMaxValida) {
        this.velMaxValida = velMaxValida;
    }

    public Long getTempsMaxAtur() {
        return tempsMaxAtur;
    }

    public void setTempsMaxAtur(Long tempsMaxAtur) {
        this.tempsMaxAtur = tempsMaxAtur;
    }

    public double getPuntsKm() {
        return puntsKm;
    }

    public void setPuntsKm(double puntsKm) {
        this.puntsKm = puntsKm;
    }

    public Long getTempsMaxRec() {
        return tempsMaxRec;
    }

    public void setTempsMaxRec(Long tempsMaxRec) {
        this.tempsMaxRec = tempsMaxRec;
    }

}