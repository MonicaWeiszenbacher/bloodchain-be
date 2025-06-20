package utcluj.stiinte.bloodchain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import utcluj.stiinte.bloodchain.data.donor.AppointmentRequest;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.repository.*;
import utcluj.stiinte.bloodchain.service.external.StorageService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DonorServiceTest {

    @MockitoBean
    private DonorRepository donorRepository;
    @MockitoBean
    private DonationRepository donationRepository;
    @MockitoBean
    private PeriodicDonationRepository periodicDonationRepository;
    @MockitoBean
    private BloodRequestRepository bloodRequestRepository;
    @MockitoBean
    private TransfusionCenterRepository transfusionCenterRepository;
    @MockitoBean
    private StorageService storageService;
    
    private DonorService donorService;
    
    @BeforeEach
    void setUp() {
        donorService = new DonorService(donorRepository, donationRepository, periodicDonationRepository,
                bloodRequestRepository, transfusionCenterRepository, storageService, null, null);
    }
    
    @Test
    @DisplayName("Should throw exception when scheduling appointment when appointment time is in the past")
    void testSaveAppointment() {
        AppointmentRequest request = new AppointmentRequest(1, 1, LocalDateTime.MIN);
        
        AppException thrown = assertThrows(AppException.class,
                () -> donorService.saveAppointment(1, request));
        
        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
    }
}
