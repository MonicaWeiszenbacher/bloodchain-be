package utcluj.stiinte.bloodchain.configuration;

import org.springframework.context.annotation.Configuration;
import utcluj.stiinte.bloodchain.service.CityService;

import java.io.IOException;

@Configuration
public class AppConfiguration {
    
    public void saveCities(CityService cityService) throws IOException {
        cityService.saveCities();
    }
}
