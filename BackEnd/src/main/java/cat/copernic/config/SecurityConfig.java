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
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)  // Deshabilitar CSRF
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/rest/rutes/**").permitAll()    
            .requestMatchers("/rest/puntgps/**").permitAll()  // Permitir todas las rutas REST
            .requestMatchers("/rest/**").permitAll()  // Permitir todas las rutas REST
            .requestMatchers("/rest/users/updatePassword/**").permitAll()
            .requestMatchers("/rest/users/update/**").permitAll()
            .requestMatchers("/rest/recompenses/**").permitAll()    
                
            .requestMatchers("/api/auth/login").permitAll()
            .requestMatchers("/api/auth/forgotPassword").permitAll()
            .requestMatchers("/api/auth/verifyToken").permitAll()
            .requestMatchers("/api/auth/verifyToken/**").permitAll()
            .requestMatchers("/api/auth/forgotPassword/**").permitAll()
            .requestMatchers("/login/**").permitAll()
            .requestMatchers("/login/forgotPassword/**").permitAll()
            .requestMatchers("/login/verifyToken/**").permitAll()
            .requestMatchers("/login/verifyToken").permitAll()
            .requestMatchers("/login/recoverPassword/**").permitAll()
            .requestMatchers("/login/recoverPassword").permitAll()
            .requestMatchers("/login/forgotPassword").permitAll()
            .requestMatchers("/styles/**", "/scripts/**", "/images/**").permitAll()
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