package utcluj.stiinte.bloodchain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import utcluj.stiinte.bloodchain.service.CityService;
import utcluj.stiinte.bloodchain.service.notification.DonationNotificationService;

import java.io.IOException;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class AppConfig {

    private final CityService cityService;
    private final DonationNotificationService periodicDonationService;

    @PostConstruct
    public void saveCities() throws IOException {
        cityService.saveCities();
    }

    @Scheduled(cron = "0 0 12 * * *") // every day at 12:00
    public void schedulePeriodicDonationTask() {
        periodicDonationService.sendNotifications();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
