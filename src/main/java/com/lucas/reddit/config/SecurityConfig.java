package com.lucas.reddit.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    /*
    * .authorizeRequests() → .authorizeHttpRequests()
        .antMatchers() → .requestMatchers()
        .access("hasAnyRole('ROLE_A','ROLE_B')") → .hasAnyRole("A", "B")
    * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/api/auth/**")
            .permitAll()
            .anyRequest()
            .authenticated()
            .and().build();
//        return http.csrf().disable()
//            .authorizeHttpRequests()
//            .requestMatchers("/admin/**")
//            .hasRole("ADMIN")
//            .requestMatchers("/protected/**")
//            .hasRole("USER")
//            .and().build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder)
        throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
