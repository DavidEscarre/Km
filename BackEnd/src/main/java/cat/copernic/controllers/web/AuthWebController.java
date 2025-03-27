/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cat.copernic.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author alpep
 */

@Controller
@RequestMapping("/login")
public class AuthWebController {

    @PostMapping
    public String LoginForm(@RequestParam("username") String username, @RequestParam("password") String password) {
    
        System.out.println("Iniciando sesión con usuario: " + username);
       

        // Redirige a la página de login (se puede modificar para que redirija según el resultado)
        return "login";
    }
    
    @GetMapping
    public String redirigirLoging() {
        return "login"; // Vista del login
    }
}