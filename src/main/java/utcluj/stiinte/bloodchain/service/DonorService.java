package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import utcluj.stiinte.bloodchain.data.appointment.DonorDonation;
import utcluj.stiinte.bloodchain.data.donor.BloodRequestData;
import utcluj.stiinte.bloodchain.data.donor.DonorDetailsResponse;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.donation.BloodRequest;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;
import utcluj.stiinte.bloodchain.model.enums.BloodRequestStatus;
import utcluj.stiinte.bloodchain.model.enums.DonationStatus;
import utcluj.stiinte.bloodchain.model.user.Donor;
import utcluj.stiinte.bloodchain.repository.BloodRequestRepository;
import utcluj.stiinte.bloodchain.repository.DonationRepository;
import utcluj.stiinte.bloodchain.repository.DonorRepository;
import utcluj.stiinte.bloodchain.repository.PeriodicDonationRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@AllArgsConstructor
public class DonorService {
    
    private final DonorRepository donorRepository;
    private final DonationRepository donationRepository;
    private final PeriodicDonationRepository periodicDonationRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final StorageService storageService;

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
                        donation.getVolume(),
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
    
    private Donor getDonor(long id) {
        return donorRepository.findById(id).orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "donor_not_found"));
    }

    private LocalDate getNextDonationDate(long donorId) {
        return periodicDonationRepository.findByDonorId(donorId)
                .map(d -> {
                    LocalDate lastDonationDate = donationRepository.findTopByDonorIdOrderByTimeDesc(donorId)
                            .map(donation -> donation.getTime().toLocalDate())
                            .orElse(null);

                    return lastDonationDate != null ? lastDonationDate.plusMonths(d.getFrequencyPerYear()) : d.getStartDate();
                })
                .orElse(null);
    }
}
