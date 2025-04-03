package cat.copernic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Deshabilitar CSRF para APIs REST
            .authorizeHttpRequests(auth -> auth
                     .requestMatchers("/rest/rutes/**").permitAll() 
                .requestMatchers("/rest/**").permitAll()  // API REST accesible sin autenticación
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/login/**", "/styles/**", "/scripts/**").permitAll()
                .anyRequest().authenticated() // Otras rutas requieren autenticación
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(401, "Unauthorized");
                }) // No redirigir a /login en APIs REST
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("/login?error=true");
                })
                .permitAll()
            )
            .logout(logout -> logout.logoutSuccessUrl("/login").permitAll());

        return http.build();
    }*/
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)  // Deshabilitar CSRF
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/rest/**").permitAll()  // Permitir todas las rutas REST
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/login/**").permitAll()
            .requestMatchers("/styles/**", "/scripts/**").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/")
            .failureHandler((request, response, exception) -> response.sendRedirect("/login?error=true"))
            .permitAll()
        );

    return http.build();
}
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}