package com.book.store.config;

import static org.springframework.security.config.Customizer.withDefaults;

import com.book.store.secutiry.JwtAuthenticationFilter;
import com.book.store.secutiry.JwtUtil;
import com.book.store.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtUtil, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        "/api/auth/register",
                                        "/api/auth/login")
                                .permitAll()
                                .requestMatchers(
                                        "/api/books/**"
                                )
                                .hasRole("USER")
                                .requestMatchers(
                                        "/api/categories/**"
                                )
                                .hasRole("USER")
                                .requestMatchers(
                                        "/api/cart/**"
                                )
                                .hasRole("USER")
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/books/"
                                )
                                .hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/books/{id}"
                                )
                                .hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/books/{id}"
                                )
                                .hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/categories"
                                )
                                .hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/api/categories/{id}"
                                )
                                .hasRole("ADMIN")
                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/api/categories/{id}"
                                )
                                .hasRole("ADMIN")
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(withDefaults())
                .userDetailsService(userDetailsService);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
