/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author alpep
 */
@Controller
public class MainController {
    
    @GetMapping("/")
    public String homePage(Model model, Authentication authentication) {

        return "index";
    }
        
    @GetMapping("/login")
    public String redirigirLoging() {
        return "login"; // Vista del login
    }
}
