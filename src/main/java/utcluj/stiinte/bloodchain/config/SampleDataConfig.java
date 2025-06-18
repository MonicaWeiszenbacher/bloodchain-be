package utcluj.stiinte.bloodchain.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import utcluj.stiinte.bloodchain.model.donation.Donation;
import utcluj.stiinte.bloodchain.model.enums.DonationStatus;
import utcluj.stiinte.bloodchain.model.enums.Role;
import utcluj.stiinte.bloodchain.model.location.Address;
import utcluj.stiinte.bloodchain.model.location.City;
import utcluj.stiinte.bloodchain.model.user.Donor;
import utcluj.stiinte.bloodchain.model.user.TransfusionCenter;
import utcluj.stiinte.bloodchain.model.user.User;
import utcluj.stiinte.bloodchain.repository.CityRepository;
import utcluj.stiinte.bloodchain.repository.DonationRepository;
import utcluj.stiinte.bloodchain.repository.TransfusionCenterRepository;
import utcluj.stiinte.bloodchain.repository.UserRepository;
import utcluj.stiinte.bloodchain.service.CityService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Contains dynamically generated sample data.
 */
@Configuration
@ConditionalOnProperty(value = "spring.profiles.active", havingValue = "local")
@AllArgsConstructor
@Import(AppConfig.class)
public class SampleDataConfig {
    
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final DonationRepository donationRepository;
    private final Random random = new Random();
    
    @PostConstruct
    @Transactional
    public void init() {
        AtomicInteger count = new AtomicInteger(1);
        
        List<TransfusionCenter> transfusionCenters = cityRepository.findAllByOrderByIdDesc().stream().map(city -> {
            TransfusionCenter center = new TransfusionCenter();
            center.setName("Centru " + count.getAndIncrement());
            center.setRole(Role.TRANSFUSION_CENTER);
            center.setEmail("c" + count.get() + "@bc.com");
            saveUser(center, city);
            return center;
        })
        .toList();

        Donor donor = new Donor();
        donor.setRole(Role.DONOR);
        donor.setEmail("testacc150525@gmail.com");
        saveUser(donor, cityRepository.findById(1L).orElseThrow());
        
        IntStream.range(0, 5).forEach(index -> saveDonation(donor, transfusionCenters.get(index)));
    }
    
    private void saveUser(User user, City city) {
        Address address = new Address();
        address.setCity(city);
        user.setAddress(address);
        user.setPassword("$2a$10$iF2JFmVdGVj7v3DtA1qPbu2YdjnFuF5xh/nTs5Ht7FRUiKMh1wn7C"); // encoded value 'password'
        userRepository.save(user);
    }
    
    private void saveDonation(Donor donor, TransfusionCenter center) {
        Donation donation = new Donation();
        donation.setStatus(DonationStatus.COMPLETED);
        donation.setTime(LocalDateTime.now());
        donation.setVolume(random.nextInt(10));
        donation.setDonor(donor);
        donation.setTransfusionCenter(center);
        donationRepository.save(donation);
    }
    
}
