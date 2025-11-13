package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class ConfigSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        // Rutas públicas
                        .requestMatchers("/public/**", "/css/**", "/js/**", "/images/**", "/login", "/error").permitAll()

                        // ADMINISTRADOR: Acceso completo a gestión del sistema
                        .requestMatchers("/admin/**").hasRole("ADMINISTRADOR")

                        // USUARIO: Puede acceder a su panel y funciones de pasabordos
                        .requestMatchers("/usuario/**").hasRole("USUARIO")

                        // Página principal o inicio
                        .requestMatchers("/", "/index").authenticated()

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/index", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/acceso-denegado")
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        // Usuario ADMINISTRADOR
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMINISTRADOR")
                .build();

        // Usuario USUARIO (estudiante/docente)
        UserDetails usuario = User.withUsername("usuario")
                .password(passwordEncoder().encode("usuario123"))
                .roles("USUARIO")
                .build();

        manager.createUser(admin);
        manager.createUser(usuario);

        return manager;
    }
}
