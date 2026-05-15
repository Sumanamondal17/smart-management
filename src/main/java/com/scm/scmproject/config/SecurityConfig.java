package com.scm.scmproject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.scm.scmproject.serviceimpl.SecurityCustomUserDetailsService;

@Configuration
public class SecurityConfig {

    private final SecurityCustomUserDetailsService userDetailService;
    private final OAuthAuthenticationSuccessHandler handler;


    // ✅ Constructor Injection (recommended instead of @Autowired)
    public SecurityConfig(SecurityCustomUserDetailsService userDetailService,
                          OAuthAuthenticationSuccessHandler handler) {
        this.userDetailService = userDetailService;
        this.handler = handler;
    }
    @Autowired
    private AuthFailurehandler authFailureHandler;

    // ✅ Authentication Provider (modern way)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailService);

        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ✅ Security Filter Chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 🔐 Register authentication provider
            .authenticationProvider(authenticationProvider())

            // 🔐 Authorization rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/**").authenticated()
                .anyRequest().permitAll()
            )

            // 🔐 Form Login
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/authenticate")
                .defaultSuccessUrl("/user/profile", true)
                .usernameParameter("email")
                .passwordParameter("password")
                .failureHandler(authFailureHandler)
            )

            // 🔐 OAuth Login (Google etc.)
            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .successHandler(handler)
            )

            // 🔐 Logout
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
            )

            // ⚠️ Disable CSRF (only for development)
            .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    // 🔑 Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}