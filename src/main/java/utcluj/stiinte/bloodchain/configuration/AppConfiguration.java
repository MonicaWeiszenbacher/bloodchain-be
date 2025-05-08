package utcluj.stiinte.bloodchain.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import utcluj.stiinte.bloodchain.service.CityService;

import java.io.IOException;

@Configuration
@AllArgsConstructor
public class AppConfiguration implements WebMvcConfigurer {
    
    private final CityService cityService;
    
    @PostConstruct
    public void saveCities() throws IOException {
        cityService.saveCities();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}