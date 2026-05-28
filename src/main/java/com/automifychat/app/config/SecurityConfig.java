package com.automifychat.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Specific authenticated GETs first
                .requestMatchers(
                        antMatcher(HttpMethod.GET, "/posts/new"),
                        antMatcher(HttpMethod.GET, "/c/new")
                ).authenticated()
                // Public reads
                .requestMatchers(
                        antMatcher(HttpMethod.GET, "/"),
                        antMatcher(HttpMethod.GET, "/login"),
                        antMatcher(HttpMethod.GET, "/signup"),
                        antMatcher(HttpMethod.GET, "/c"),
                        antMatcher(HttpMethod.GET, "/c/*"),
                        antMatcher(HttpMethod.GET, "/posts/*"),
                        antMatcher(HttpMethod.GET, "/u/*"),
                        antMatcher(HttpMethod.GET, "/search"),
                        antMatcher(HttpMethod.GET, "/css/**"),
                        antMatcher(HttpMethod.GET, "/images/**"),
                        antMatcher(HttpMethod.GET, "/webjars/**"),
                        antMatcher(HttpMethod.GET, "/favicon.ico"),
                        antMatcher("/error")
                ).permitAll()
                // Auth-form POSTs
                .requestMatchers(
                        antMatcher(HttpMethod.POST, "/login"),
                        antMatcher(HttpMethod.POST, "/signup"),
                        antMatcher(HttpMethod.POST, "/logout")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?loggedOut")
                .permitAll()
            );
        return http.build();
    }
}
