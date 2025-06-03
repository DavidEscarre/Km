package com.example.km.core.models;

import java.time.LocalDateTime;

public class PuntGPS {

    private Long id;


    private Ruta ruta;


    private double latitud;


    private double longitud;


    private LocalDateTime marcaTemps;

    public PuntGPS() {
    }

    public PuntGPS(Ruta ruta, double latitud, double longitud, LocalDateTime marcaTemps) {
        this.ruta = ruta;
        this.latitud = latitud;
        this.longitud = longitud;
        this.marcaTemps = marcaTemps;
    }
    public PuntGPS(double latitud, double longitud, LocalDateTime marcaTemps) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.marcaTemps = marcaTemps;
    }

    public PuntGPS(Long id, Ruta ruta, double latitud, double longitud, LocalDateTime marcaTemps) {
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

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public LocalDateTime getMarcaTemps() {
        return marcaTemps;
    }

    public void setMarcaTemps(LocalDateTime marcaTemps) {
        this.marcaTemps = marcaTemps;
    }







}