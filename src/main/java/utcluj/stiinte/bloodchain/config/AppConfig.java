package utcluj.stiinte.bloodchain.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import utcluj.stiinte.bloodchain.model.donation.PeriodicDonation;
import utcluj.stiinte.bloodchain.model.location.City;
import utcluj.stiinte.bloodchain.repository.CityRepository;
import utcluj.stiinte.bloodchain.repository.PeriodicDonationRepository;
import utcluj.stiinte.bloodchain.service.external.GoogleService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class AppConfig {
    
    private final PeriodicDonationRepository periodicDonationRepository;
    private final GoogleService googleService;
    private final CityRepository cityRepository;

    @PostConstruct
    public void saveCities() throws IOException {
        if (cityRepository.count() > 0) {
            return;
        }

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setSkipHeaderRecord(true)
                .setHeader("city", "city_ascii", "lat", "lng", "country", "iso2", "iso3", "admin_name", "capital", "population", "id")
                .setQuote('"')
                .setDelimiter(",")
                .get();

        try (InputStream stream = new ClassPathResource("cities/world-cities.csv").getInputStream()) {
            csvFormat.parse(new BufferedReader(new InputStreamReader(stream, Charset.defaultCharset())))
                    .stream()
                    .forEach(csv -> {
                        City city = new City();
                        city.setName(csv.get("city"));
                        city.setCountry(csv.get("country"));
                        city.setLatitude(Double.parseDouble(csv.get("lat")));
                        city.setLongitude(Double.parseDouble(csv.get("lng")));
                        cityRepository.save(city);
                    });
        }
    }

    @Scheduled(cron = "0 0 12 * * *") // every day at 12:00
    public void schedulePeriodicDonationTask() {
        List<PeriodicDonation> periodicDonations = periodicDonationRepository.findAllByNotificationSentIsFalse();

        periodicDonations.forEach(pd -> {
            googleService.createPeriodicEvent(pd);
            pd.setNotificationSent(true);
            periodicDonationRepository.save(pd);
        });
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
}
