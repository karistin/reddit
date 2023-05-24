package com.lucas.reddit.config;

import com.lucas.reddit.service.UserDetailServiceImpl;
import lombok.AllArgsConstructor;
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
//@AllArgsConstructor
public class SecurityConfig {

//    private final UserDetailsService userDetailsService;

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
//   https://hou27.tistory.com/entry/Spring-Security-%EC%84%B8%EC%85%98-%EC%9D%B8%EC%A6%9D
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder)
//        throws Exception {
//        authenticationManagerBuilder.userDetailsService(userDetailsService)
//            .passwordEncoder(passwordEncoder());
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    https://www.baeldung.com/spring-deprecated-websecurityconfigureradapter
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity,
        UserDetailServiceImpl userDetailsService
    ) throws Exception {

        System.out.println("AuthenticationManager 생성");
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder())
            .and()
            .build();
    }

}
