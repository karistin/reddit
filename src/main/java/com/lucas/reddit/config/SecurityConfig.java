package com.lucas.reddit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
