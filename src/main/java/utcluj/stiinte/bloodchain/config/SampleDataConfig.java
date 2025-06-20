package utcluj.stiinte.bloodchain.config;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import utcluj.stiinte.bloodchain.model.donation.BloodRequest;
import utcluj.stiinte.bloodchain.model.donation.Donation;
import utcluj.stiinte.bloodchain.model.enums.*;
import utcluj.stiinte.bloodchain.model.location.Address;
import utcluj.stiinte.bloodchain.model.location.City;
import utcluj.stiinte.bloodchain.model.user.Donor;
import utcluj.stiinte.bloodchain.model.user.TransfusionCenter;
import utcluj.stiinte.bloodchain.model.user.User;
import utcluj.stiinte.bloodchain.repository.BloodRequestRepository;
import utcluj.stiinte.bloodchain.repository.CityRepository;
import utcluj.stiinte.bloodchain.repository.DonationRepository;
import utcluj.stiinte.bloodchain.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    private final BloodRequestRepository bloodRequestRepository;
    private final Map<BloodGroup, Integer> bloodStock = Map.of(
            BloodGroup.A_NEGATIVE, 10,
            BloodGroup.AB_POSITIVE, 10
    );

    @PostConstruct
    @Transactional
    public void init() {
        AtomicInteger count = new AtomicInteger(1);

        Donor donor = new Donor();
        donor.setRole(Role.DONOR);
        donor.setFirstName("Ana");
        donor.setLastName("Pop");
        donor.setEmail("weiszenbacher.io.mo@student.utcluj.ro");
        donor.setBloodGroup(randomBloodGroup());
        donor.setBirthDate(LocalDate.of(2000, 1, 1));
        donor.setGender(Gender.FEMALE);
        saveUser(donor, cityRepository.findByName("Cluj-Napoca"));
        
        List<TransfusionCenter> transfusionCenters = cityRepository.findAllByOrderByIdDesc().stream().map(city -> {
            TransfusionCenter center = new TransfusionCenter();
            center.setName("Centru " + count.getAndIncrement());
            center.setRole(Role.TRANSFUSION_CENTER);
            center.setEmail("testacc150525@gmail.com");
            center.setBloodStock(bloodStock);
            saveUser(center, city);
            return center;
        })
        .toList();
        
        IntStream.range(1, transfusionCenters.size()).forEach(index -> saveDonation(donor, transfusionCenters.get(index)));
        saveBloodRequests(donor, transfusionCenters.getFirst());
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
        donation.setUnits(random.nextInt(10));
        donation.setDonor(donor);
        donation.setTransfusionCenter(center);
        donationRepository.save(donation);
    }
    
    private void saveBloodRequests(Donor requester, TransfusionCenter center) {
        BloodRequest firstRequest = new BloodRequest();
        firstRequest.setRequester(requester);
        firstRequest.setBloodGroup(BloodGroup.A_NEGATIVE);
        firstRequest.setUnits(2);
        firstRequest.setTakeOverDate(LocalDate.now().plusDays(random.nextInt(10)));
        firstRequest.setStatus(BloodRequestStatus.PENDING);
        firstRequest.setTransfusionCenter(center);
        bloodRequestRepository.save(firstRequest);

        BloodRequest secondRequest = new BloodRequest();
        secondRequest.setRequester(requester);
        secondRequest.setBloodGroup(BloodGroup.AB_POSITIVE);
        secondRequest.setUnits(10);
        secondRequest.setTakeOverDate(LocalDate.now().plusDays(random.nextInt(10)));
        secondRequest.setStatus(BloodRequestStatus.PENDING);
        secondRequest.setTransfusionCenter(center);
        bloodRequestRepository.save(secondRequest);
    }
    
    private BloodGroup randomBloodGroup() {
        return BloodGroup.values()[random.nextInt(BloodGroup.values().length)];
    }
    
}
