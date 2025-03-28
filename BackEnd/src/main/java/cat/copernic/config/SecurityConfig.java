package cat.copernic.config;

import cat.copernic.Entity.User;
import cat.copernic.config.ValidadorUsuaris;
import cat.copernic.enums.Rol;
import cat.copernic.logica.UserLogic;
import cat.copernic.repository.UserRepo;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private UserLogic userLogic;
    
    @Autowired
    private UserRepo userRepo;

   @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF (recomendado solo para APIs REST)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/login").permitAll()
           .requestMatchers("/login/**").permitAll() // Acceso público
                .requestMatchers("/styles/**", "/scripts/**").permitAll()   // Acceso público a recursos estáticos
            .anyRequest().authenticated()  // El resto de rutas requieren autenticación
        )
       
        .formLogin(form -> form
            .loginPage("/login")  // Página de login web
            .usernameParameter("username")
            .passwordParameter("password")
            .defaultSuccessUrl("/")
            //.failureUrl("/login?error=true")
                .failureHandler((request, response, exception) -> {
                 response.sendRedirect("/login?error=true");
                })
            .permitAll()
        );

    return http.build();
}

   @Bean
    public AuthenticationManager authManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(new ValidadorUsuaris())
            .passwordEncoder(passwordEncoder)            
            .and()
            .build();
    
    }
    /*
    private void crearAdmin(){
        User admin = new User();
        admin.setRol(Rol.ADMIN);
        admin.setNom("admin");
        admin.setEmail("admin");
        admin.setDataAlta(now());
        admin.setEstat(true);
        admin.setObservacions("");
        admin.setSaldoDisponible(0);
        admin.setTelefon(9999999);
        admin.setAdreca("vilafranca");
        admin.setWord("$2a$10$e1CNWDQut/QZFfD3Gvy13eAcGcvnlv7XVXnsr5cRxFcpn7UuhsJL6"); 
        
        userRepo.save(admin);
        
    }*/
    @Bean
    public PasswordEncoder passwordEncoder(){
         return new BCryptPasswordEncoder();
    }
}