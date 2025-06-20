package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utcluj.stiinte.bloodchain.data.authentication.RegisterRequest;
import utcluj.stiinte.bloodchain.data.donor.*;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.donation.BloodRequest;
import utcluj.stiinte.bloodchain.model.donation.Donation;
import utcluj.stiinte.bloodchain.model.donation.PeriodicDonation;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;
import utcluj.stiinte.bloodchain.model.enums.BloodRequestStatus;
import utcluj.stiinte.bloodchain.model.enums.DonationStatus;
import utcluj.stiinte.bloodchain.model.user.Donor;
import utcluj.stiinte.bloodchain.model.user.TransfusionCenter;
import utcluj.stiinte.bloodchain.repository.*;
import utcluj.stiinte.bloodchain.service.external.GoogleService;
import utcluj.stiinte.bloodchain.service.external.StorageService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class DonorService {
    
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;
    private final PeriodicDonationRepository periodicDonationRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final TransfusionCenterRepository transfusionCenterRepository;
    private final StorageService storageService;
    private final GoogleService googleService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public DonorDetailsResponse getUserDetails(long id) {
        Donor donor = getDonor(id);
        
        int numberOfTokens = donationRepository.countAllByDonorIdAndTokenIsNotNull(id);
        
        return new DonorDetailsResponse(
                donor.getFirstName() + StringUtils.SPACE + donor.getLastName(),
                donor.getEmail(),
                Period.between(donor.getBirthDate(), LocalDate.now()).getYears(),
                donor.getGender().name(),
                donor.getBloodGroup().name(),
                numberOfTokens,
                getNextDonationDate(id),
                null);
    }
    
    @Transactional(readOnly = true)
    public List<DonorDonation> getDonationHistory(long donorId) {
        return donationRepository.findAllByDonorIdAndStatusOrderByTimeDesc(donorId, DonationStatus.COMPLETED)
                .stream()
                .map(donation -> new DonorDonation(
                        donation.getId(),
                        donation.getTime(),
                        donation.getTransfusionCenter().getName() + " - " + donation.getTransfusionCenter().getAddress().getCity().getName(),
                        donation.getUnits(),
                        donation.getToken()
                ))
                .toList();
    }
    
    @Transactional
    public void saveMedicalFile(long donorId, MultipartFile file) {
        Donor donor = getDonor(donorId);
        donor.setMedicalFileName(file.getOriginalFilename());
        storageService.store(file);
    }
    
    @Transactional
    public void requestBlood(long donorId, BloodRequestData requestData) {
        BloodRequest bloodRequest = new BloodRequest();
        bloodRequest.setRequester(getDonor(donorId));
        bloodRequest.setBloodGroup(BloodGroup.parse(requestData.bloodGroup()));
        bloodRequest.setUnits(requestData.units());
        bloodRequest.setTakeOverDate(requestData.takeoverDate());
        bloodRequest.setStatus(BloodRequestStatus.PENDING);
        bloodRequestRepository.save(bloodRequest);
    }

    @Transactional
    public void saveAppointment(long donorId, AppointmentRequest request) {
        Donation donation = new Donation();
        
        if (request.time().isBefore(LocalDateTime.now())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "appointment_invalid_time");
        }
        
        donation.setDonor(getDonor(donorId));
        donation.setTransfusionCenter(getCenter(request.transfusionCenterId()));
        donation.setTime(request.time());
        donationRepository.save(donation);
        googleService.sendAppointmentEmail(donation);
    }
    
    @Transactional
    public void savePeriodicDonation(long donorId, PeriodicDonationRequest request) {
        PeriodicDonation donation = new PeriodicDonation();
        donation.setDonor(getDonor(donorId));
        donation.setTransfusionCenter(getCenter(request.transfusionCenterId()));
        donation.setStartDate(request.startDate());
        donation.setStartDate(LocalDateTime.now());
        donation.setEndDate(request.startDate().plusYears(1));
        donation.setFrequencyPerYear(request.frequency());
        donation.setTitle("Donation");
        
        try {
            googleService.createPeriodicEvent(donation);
            donation.setNotificationSent(true);
        } catch (RuntimeException e) {
            log.error("Could not create periodic event", e);
            donation.setNotificationSent(false);
        }
        
        periodicDonationRepository.save(donation);
    }
    
    private Donor getDonor(long id) {
        return donorRepository.findById(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "donor_not_found"));
    }
    
    private TransfusionCenter getCenter(long id) {
        return transfusionCenterRepository.findById(id).orElseThrow(() ->
                new AppException(HttpStatus.NOT_FOUND, "transfusion_center_not_found"));
    }

    private LocalDate getNextDonationDate(long donorId) {
        return periodicDonationRepository.findByDonorId(donorId)
                .map(d -> {
                    LocalDate lastDonationDate = donationRepository.findTopByDonorIdOrderByTimeDesc(donorId)
                            .map(donation -> donation.getTime().toLocalDate())
                            .orElse(null);

                    return lastDonationDate != null 
                            ? lastDonationDate.plusMonths(d.getFrequencyPerYear())
                            : d.getStartDate().toLocalDate();
                })
                .orElse(null);
    }

    public void saveDonor(RegisterRequest request) {
        Donor donor = new Donor();
        donor.setEmail(request.getEmail());
        donor.setPassword(passwordEncoder.encode(request.getPassword()));
        donor.setGender(request.getGender());
        donor.setBloodGroup(request.getBloodGroup());
        donorRepository.save(donor);
    }
}
