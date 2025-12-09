package chubby.teu.tuda.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // <-- Import dòng này
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Import thêm cho CORS
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))

                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/login",
                                "/api/register")
                        .permitAll()
                        // Admin/Founder/Employee for user management
                        .requestMatchers(HttpMethod.GET, "/api/users", "/api/users/**").hasAnyAuthority("ADMIN", "FOUNDER", "EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/users", "/api/users/**").hasAnyAuthority("ADMIN", "FOUNDER", "EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyAuthority("ADMIN", "FOUNDER", "EMPLOYEE")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasAnyAuthority("ADMIN", "FOUNDER", "EMPLOYEE")
                        // Admin/Founder/Employee for order management
                        .requestMatchers("/api/admin/orders/**").hasAnyAuthority("ADMIN", "FOUNDER", "EMPLOYEE")
                        // Authenticated users can access their own orders
                        .requestMatchers("/api/orders/**").authenticated()
                        // Admin/Founder only for category management (specific patterns first)
                        .requestMatchers(HttpMethod.POST, "/api/categories", "/api/categories/**").hasAnyAuthority("ADMIN", "FOUNDER")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasAnyAuthority("ADMIN", "FOUNDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasAnyAuthority("ADMIN", "FOUNDER")
                        // Admin/Founder only for product management (specific patterns first)
                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasAnyAuthority("ADMIN", "FOUNDER")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAnyAuthority("ADMIN", "FOUNDER")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAnyAuthority("ADMIN", "FOUNDER")
                        // Public read-only access (after specific rules)
                        .requestMatchers(HttpMethod.GET, "/api/products/**", "/api/categories/**", "/api/categories").permitAll()
                        .anyRequest().authenticated());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}