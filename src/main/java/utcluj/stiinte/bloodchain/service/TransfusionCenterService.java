package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utcluj.stiinte.bloodchain.data.appointment.AppointmentRequest;
import utcluj.stiinte.bloodchain.data.appointment.TransfusionCenterAppointmentData;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.donation.Donation;
import utcluj.stiinte.bloodchain.model.enums.DonationStatus;
import utcluj.stiinte.bloodchain.model.user.Donor;
import utcluj.stiinte.bloodchain.model.user.TransfusionCenter;
import utcluj.stiinte.bloodchain.repository.DonationRepository;
import utcluj.stiinte.bloodchain.repository.DonorRepository;
import utcluj.stiinte.bloodchain.repository.TransfusionCenterRepository;
import utcluj.stiinte.bloodchain.data.TransfusionCenterData;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class TransfusionCenterService {
    
    private final DonationRepository donationRepository;
    private final DonorRepository donorRepository;
    private final TransfusionCenterRepository transfusionCenterRepository;
    private final CityService cityService;
    
    @Transactional(readOnly = true)
    public List<TransfusionCenterData> getTransfusionCenters(long userId) {
        Donor donor = donorRepository.findById(userId).orElseThrow();

        return transfusionCenterRepository.findTop10ClosestTransfusionCenters(donor.getId());
    }

    @Transactional
    public void saveAppointment(AppointmentRequest request) {
        Donation donation = new Donation();
        setAppointmentDetails(donation, request);
        donationRepository.save(donation);
    }
    
    @Transactional
    public void updateAppointment(long appointmentId, AppointmentRequest request) {
        Donation donation = getAppointment(appointmentId);
        setAppointmentDetails(donation, request);
        donationRepository.save(donation);
    }
    
    public void deleteAppointment(long appointmentId) {
        donationRepository.delete(getAppointment(appointmentId));
    }
    
    public List<TransfusionCenterAppointmentData> getTransfusionCenterAppointments(
            long transfusionCenterId, DonationStatus status) {
        return donationRepository.findAllByTransfusionCenterIdAndStatusOrderByTimeDesc(transfusionCenterId, status)
                .stream()
                .map(a -> new TransfusionCenterAppointmentData(
                        a.getId(),
                        a.getTime(),
                        a.getDonor().getBloodGroup(),
                        a.getDonor().getAddress().getPhone()
                )).toList();
    }
    
    private void setAppointmentDetails(Donation donation, AppointmentRequest request) {
        if (request.time().isBefore(LocalDateTime.now())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "appointment_invalid_time");
        }

        Donor donor = donorRepository
                .findById(request.userId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "donor_not_found"));

        TransfusionCenter transfusionCenter = transfusionCenterRepository
                .findById(request.transfusionCenterId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "transfusion_center_not_found"));
        
        donation.setDonor(donor);
        donation.setTransfusionCenter(transfusionCenter);
        donation.setTime(request.time());
    }
    
    private Donation getAppointment(long appointmentId) {
        return donationRepository
                .findById(appointmentId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "appointment_not_found"));
    }
}
