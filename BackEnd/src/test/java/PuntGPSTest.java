/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import cat.copernic.Entity.PuntGPS;
import cat.copernic.KmApplication;
import cat.copernic.logica.PuntGPSLogic;
import cat.copernic.repository.PuntGPSRepo;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author alpep
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = KmApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PuntGPSTest {
    @Autowired
    private PuntGPSLogic puntGPSLogic;

    @Autowired
   private PuntGPSRepo puntGPSRepo;
    
     @LocalServerPort
    private int port;
     @Autowired
    private TestRestTemplate restTemplate;
    
    public PuntGPSTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    
    @Test
    public void hello() {
    
    }
    
    @Test
    public void testGetAdByIdOk() throws Exception {
        
       // List<PuntGPS> puntsGPS = puntGPSLogic.getPuntsGPSyRuteId(Integer.toUnsignedLong(19));
        String url = "http://localhost:" + port + "/rest/puntgps/byIdRuta/" + Integer.toUnsignedLong(19);

        ResponseEntity<List<PuntGPS>> response;
        response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PuntGPS>>() {});

        List<PuntGPS> punts = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        System.out.println(response.getBody().size());
        assertEquals(21, response.getBody().size());
    }
}




