package chubby.teu.tuda.infrastructure.config; // Giữ package của bạn

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Cấu hình CORS cho API Manager
                registry.addMapping("/api/manager/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .maxAge(3600);

                // THÊM CẤU HÌNH CORS CHO CÁC API PUBLIC
                registry.addMapping("/api/products/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .maxAge(3600);

                // Bạn cũng có thể thêm cho các API public khác nếu cần
                registry.addMapping("/api/categories/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }
}