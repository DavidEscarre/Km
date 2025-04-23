/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.logica;

import cat.copernic.Entity.PuntGPS;
import cat.copernic.Entity.Ruta;
import cat.copernic.controllers.API.UserApiController;
import cat.copernic.repository.RutaRepo;
import cat.copernic.repository.UserRepo;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author alpep
 */
@Service
public class RutaLogic {
    
    @Autowired
    private RutaRepo rutaRepo;
    Logger logger = LoggerFactory.getLogger(UserApiController.class);
    
    
    public List<Ruta> findAllRutes() throws Exception{
        List<Ruta> ret = new ArrayList<>();
        
        ret = rutaRepo.findAll();
        
        return ret;
        
    }
    
    public List<Ruta> getAllByCiclistaEmail(String email)throws Exception{
        
        List<Ruta> ret = rutaRepo.findAllByCiclista_Email(email);
        
        return ret;
    }
    
    public Ruta getRuta(Long id)throws Exception{
        
        Ruta ret = rutaRepo.findById(id).orElse(null);
        
        return ret;
    }
    public boolean existsById(Long id)throws Exception
    {
        Ruta r = rutaRepo.findById(id).orElse(null);
        
        return (r != null);
    }    
    
    
    public Long saveRuta(Ruta ruta)throws Exception{

        Long rutaId = rutaRepo.save(ruta).getId();
            
        return rutaId;

    }
    public Long updateRuta(Ruta ruta)throws Exception{
       
        Ruta OldRuta = rutaRepo.findById(ruta.getId()).orElse(null);
        
        if(OldRuta==null){
            return null;
        }
        
       OldRuta.setCiclista(ruta.getCiclista());
       OldRuta.setDataFinal(ruta.getDataFinal());
       OldRuta.setDataInici(ruta.getDataInici());
       OldRuta.setEstat(ruta.getEstat());

       OldRuta.setDistancia(ruta.getDistancia());
       OldRuta.setSaldo(ruta.getSaldo());
       OldRuta.setVelocitatMax(ruta.getVelocitatMax());
       OldRuta.setVelocitatMitjana(ruta.getVelocitatMitjana());
      // OldRuta.setPuntsGPS(ruta.getPuntsGPS());
       
       logger.info("Oldruta Dist: "+OldRuta.getDistancia());
       logger.info("Oldruta velMAX: "+OldRuta.getVelocitatMax());
       logger.info("Oldruta velMIG: "+OldRuta.getVelocitatMitjana());
       logger.info("Oldruta sALD: "+OldRuta.getSaldo());
       return rutaRepo.save(OldRuta).getId();
        
    }
   
        
     
/*
    public void calcularMetrics(Ruta ruta) {
        final double RADIO_TIERRA = 6371000; // en metros
        ZoneId zoneId = ZoneId.systemDefault();

        double distancia = 0.0;
        double velMax = 0.0;

        List<PuntGPS> puntsGPS = ruta.getPuntsGPS();

        for (int i = 0; i < puntsGPS.size() - 1; i++) {
            PuntGPS a = puntsGPS.get(i);
            PuntGPS b = puntsGPS.get(i + 1);

            if (a != null && b != null) {
                double d = calcularDistanciaHaversine(
                        a.getLatitud() , a.getLongitud(),
                        b.getLatitud(), b.getLongitud()
                );

                distancia += d;

                long aTimeMillis = a.getMarcaTemps().atZone(zoneId).toInstant().toEpochMilli();
                long bTimeMillis = b.getMarcaTemps().atZone(zoneId).toInstant().toEpochMilli();

                double abTimeSeg = (bTimeMillis - aTimeMillis) / 1000.0;
                if (abTimeSeg > 0) {
                    double abVel = (d / abTimeSeg) * 3.6;
                    if (abVel > velMax) {
                        velMax = abVel;
                    }
                }
            }
            
            
        }

        long startMillis = ruta.getDataInici().atZone(zoneId).toInstant().toEpochMilli();
        long endMillis = ruta.getDataFinal().atZone(zoneId).toInstant().toEpochMilli();

        double durada = (endMillis - startMillis) / 1000.0;
        double velMig = (distancia / durada) * 3.6;
        distancia /= 1000.0;
        double saldo = distancia * 1;

        ruta.setSaldo(saldo);
        ruta.setDistancia(distancia);
        ruta.setVelocitatMax(velMax);
        ruta.setVelocitatMitjana(velMig);
        
        
        
        
        
    }

    // Fórmula de Haversine
    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 6371000 * c;
    }*/
}

