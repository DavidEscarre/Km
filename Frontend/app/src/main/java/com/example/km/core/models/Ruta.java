package com.example.km.core.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.km.core.utils.enums.EstatRuta;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Ruta {
    private Long id;

    private User ciclista;


    private EstatRuta estat = EstatRuta.PENDENT;


    private LocalDateTime dataInici;

    private LocalDateTime dataFinal;


    private double saldo = 0.00;


    private double distancia = 0.00;


    private double velocitatMitjana = 0.00;


    private double velocitatMax = 0.00;

    private List<PuntGPS> puntsGPS = new ArrayList<>();

    private long tempsAturat = 0L;

    public Ruta() {
    }
    public Ruta(Long id) {
        this.id = id;
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

    public Ruta(Long id, User ciclista, LocalDateTime dataInici, LocalDateTime dataFinal, Long tempsAturat) {
        this.id = id;
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;
        this.tempsAturat = tempsAturat;

    }

    public Ruta(Long id, User ciclista, LocalDateTime dataInici, LocalDateTime dataFinal) {
        this.id = id;
        this.ciclista = ciclista;
        this.dataInici = dataInici;
        this.dataFinal = dataFinal;

    }

    public Ruta(User ciclista, LocalDateTime dataInici, LocalDateTime dataFinal,List<PuntGPS> puntsGPS) {
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
    public Ruta(User ciclista, LocalDateTime dataInici) {
        this.ciclista = ciclista;
        this.dataInici = dataInici;



    }

    public long getTempsAturat() {
        return tempsAturat;
    }

    public void setTempsAturat(long tempsAturat) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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

/*
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDurada() {

        // Validación en caso de que dataFinal sea nulo
        if (this.dataInici == null || this.dataFinal == null) {
            return " - ";
        }

        ZoneId zoneId = ZoneId.systemDefault();

        long startMillis = this.dataInici.atZone(zoneId).toInstant().toEpochMilli();
        long endMillis = this.dataFinal.atZone(zoneId).toInstant().toEpochMilli();

        long durada = (endMillis - startMillis) / 1000;


        long hores = durada / 3600;
        long minuts = (durada % 3600 ) /60;
        long segons = durada-(hores*3600)- minuts;
        // Formateo a "Xh Ym"
        return  String.format("%dh %02dmin", hores, minuts);

    }*/

}