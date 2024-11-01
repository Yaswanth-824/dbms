package com.example.Travel.And.Tourisum.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.Travel.And.Tourisum.service.userservice.authservice;
@Configuration
@EnableWebSecurity
public class securityconfig{


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoder for hashing
    }
    @Bean
    public UserDetailsService userDetailsService(){
        return new authservice();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
            .disable() // Example: ignore CSRF for API calls, if necessary
        )
            // provide Authorization
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/","/register", "/login", "/newUser", "/place/**","/css/**","/uploads/**","/save","/All").permitAll()         // Allow access to "/"
            .anyRequest().authenticated() // Require login for "/Home/**" and all its sub-paths
            )   
            // login form page
            .formLogin(form -> form
                .loginPage("/login")   
                .defaultSuccessUrl("/")                   // Specify the custom login page URL
                .permitAll()                              // Allow everyone to see the login page
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/") 
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()                              // Allow everyone to access logout functionality
            ); // Require authentication for any other requests
        return http.build();
    }
}
