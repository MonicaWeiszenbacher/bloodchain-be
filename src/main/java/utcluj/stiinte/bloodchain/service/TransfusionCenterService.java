package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utcluj.stiinte.bloodchain.data.transfusioncenter.*;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.donation.BloodRequest;
import utcluj.stiinte.bloodchain.model.donation.Donation;
import utcluj.stiinte.bloodchain.model.enums.BloodGroup;
import utcluj.stiinte.bloodchain.model.enums.BloodRequestStatus;
import utcluj.stiinte.bloodchain.model.enums.DonationStatus;
import utcluj.stiinte.bloodchain.model.user.Donor;
import utcluj.stiinte.bloodchain.repository.BloodRequestRepository;
import utcluj.stiinte.bloodchain.repository.DonationRepository;
import utcluj.stiinte.bloodchain.repository.DonorRepository;
import utcluj.stiinte.bloodchain.repository.TransfusionCenterRepository;
import utcluj.stiinte.bloodchain.service.blockchain.BlockchainService;
import utcluj.stiinte.bloodchain.service.external.GoogleService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TransfusionCenterService {
    
    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final TransfusionCenterRepository transfusionCenterRepository;
    private final BloodRequestRepository bloodRequestRepository;
    private final GoogleService googleService;
    private final BlockchainService blockchainService;

    @Transactional(readOnly = true)
    public List<TransfusionCenterData> getTransfusionCenters(long userId) {
        Donor donor = donorRepository.findById(userId).orElseThrow();

        return transfusionCenterRepository.findTop10ClosestTransfusionCenters(donor.getId());
    }
    
    public List<TransfusionCenterDonation> getDonations(long id) {
        return donationRepository.findAllByTransfusionCenterIdAndStatusOrderByTimeDesc(id, DonationStatus.COMPLETED)
                .stream()
                .map(donation -> new TransfusionCenterDonation(
                        donation.getId(),
                        donation.getTime(),
                        donation.getDonor().getId(),
                        donation.getDonor().getBloodGroup(),
                        donation.getUnits(),
                        donation.getToken()
                )).toList();
    }

    @Transactional
    public void saveAppointment(long transfusionCenterId, AppointmentRequest request) {
        Donation donation = new Donation();

        if (request.time().isBefore(LocalDateTime.now())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "appointment_invalid_time");
        }

        donation.setDonor(donorRepository.findById(request.donorId()).orElseThrow());
        donation.setTransfusionCenter(transfusionCenterRepository.findById(transfusionCenterId).orElseThrow());
        donation.setTime(request.time());
        donationRepository.save(donation);
        googleService.sendAppointmentEmail(donation);
    }
    
    public List<DonorData> getDonors(long id) {
        return donationRepository.findAllByTransfusionCenterId(id)
                .stream()
                .map(Donation::getDonor)
                .map(donor -> new DonorData(
                        donor.getFirstName() + StringUtils.SPACE + donor.getLastName(),
                        donor.getEmail(),
                        donor.getBloodGroup(),
                        Period.between(donor.getBirthDate(), LocalDate.now()).getYears(),
                        donor.getGender()
                )).toList();
    }
    
    public List<BloodRequestData> getBloodRequests(long id) {
        return bloodRequestRepository.findBloodRequestByTransfusionCenterId(id)
                .stream()
                .map(request -> new BloodRequestData(
                        request.getId(),
                        request.getRequester().getId(), 
                        request.getTransfusionCenter().getBloodStock().getOrDefault(request.getBloodGroup(), 0),
                        request.getBloodGroup(),
                        request.getUnits(),
                        request.getTakeOverDate()
                ))
                .toList();
    }
    
    @Transactional
    public void updateBloodRequest(long id, BloodRequestStatus status) {
        BloodRequest bloodRequest = bloodRequestRepository.findById(id).orElseThrow();
        bloodRequest.setStatus(status);
        
        if (status == BloodRequestStatus.APPROVED) {
            Map< BloodGroup, Integer> bloodStock = bloodRequest.getTransfusionCenter().getBloodStock();
            bloodStock.put(bloodRequest.getBloodGroup(), Math.max(
                    bloodStock.getOrDefault(bloodRequest.getBloodGroup(), 0) - bloodRequest.getUnits(), 0));
        }
    }

    public void sendTokenToDonor(long donationId) {
        Donation donation = donationRepository.findById(donationId).orElseThrow();
        blockchainService.sendTokenToDonor(donation.getDonor());
    }
}
