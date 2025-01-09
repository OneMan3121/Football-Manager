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
        // Налаштування HttpSecurity
        http
                .csrf(csrf -> csrf.disable()) // CSRF вимкнено для зручності
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/v1/public/**").permitAll() // Відкриті ендпоінти
                        .anyRequest().authenticated() // Усі інші запити потребують авторизації
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())) // Налаштування JWT
                );

        return http.build();
    }

    /**
     * Конвертер для роботи з ролями/групами Cognito.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Додаємо стандартний префікс "ROLE_" для груп
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("cognito:groups"); // Групи з токена Cognito

        JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
        authenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return authenticationConverter;
    }
}