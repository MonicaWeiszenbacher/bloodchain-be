package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utcluj.stiinte.bloodchain.model.location.City;
import utcluj.stiinte.bloodchain.model.user.User;
import utcluj.stiinte.bloodchain.repository.CityRepository;
import utcluj.stiinte.bloodchain.repository.UserRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class CityService {
    
    private final CityRepository cityRepository;
    
    @Transactional
    public void saveCities() throws IOException {
        if (cityRepository.count() > 0) {
            return;
        }

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setSkipHeaderRecord(true)
                .setMaxRows(10)
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
}
