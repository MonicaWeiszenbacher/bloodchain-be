package utcluj.stiinte.bloodchain.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import utcluj.stiinte.bloodchain.service.CityService;
import utcluj.stiinte.bloodchain.service.PeriodicDonationService;

import java.io.IOException;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class AppConfig {

    private final CityService cityService;
    private final PeriodicDonationService periodicDonationService;

    @PostConstruct
    public void saveCities() throws IOException {
        cityService.saveCities();
    }

    @Scheduled(cron = "0 0 12 * * *") // every day at 12:00
    public void schedulePeriodicDonationTask() {
        periodicDonationService.sendNotifications();
    }
}
