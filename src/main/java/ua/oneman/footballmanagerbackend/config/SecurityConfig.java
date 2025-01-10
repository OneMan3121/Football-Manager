package ua.oneman.footballmanagerbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Вимкнення CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/public/**").permitAll() // Відкриті ендпоінти
                        .anyRequest().authenticated() // Усі інші запити потребують валідного токена
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())) // Налаштування JWT
                );

        return http.build();
    }

    // Конвертер аутентифікації
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        // Використовуємо стандартний конвертер для мінімальних потреб – без ролей
        return new JwtAuthenticationConverter();
    }
}