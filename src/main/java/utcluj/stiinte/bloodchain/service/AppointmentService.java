package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utcluj.stiinte.bloodchain.data.appointment.AppointmentRequest;
import utcluj.stiinte.bloodchain.data.appointment.DonationHistory;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.Appointment;
import utcluj.stiinte.bloodchain.model.Donor;
import utcluj.stiinte.bloodchain.model.TransfusionCenter;
import utcluj.stiinte.bloodchain.model.enums.AppointmentStatus;
import utcluj.stiinte.bloodchain.repository.AppointmentRepository;
import utcluj.stiinte.bloodchain.repository.DonorRepository;
import utcluj.stiinte.bloodchain.repository.TransfusionCenterRepository;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final DonorRepository donorRepository;
    private final TransfusionCenterRepository transfusionCenterRepository;

    @Transactional
    public void saveAppointment(AppointmentRequest request) {
        Appointment appointment = new Appointment();
        setAppointmentDetails(appointment, request);
        appointmentRepository.save(appointment);
    }
    
    @Transactional
    public void updateAppointment(long appointmentId, AppointmentRequest request) {
        Appointment appointment = getAppointment(appointmentId);
        setAppointmentDetails(appointment, request);
        appointmentRepository.save(appointment);
    }
    
    public void deleteAppointment(long appointmentId) {
        appointmentRepository.delete(getAppointment(appointmentId));
    }
    
    public List<Appointment> getAppointments(long transfusionCenterId) {
        return appointmentRepository.findAllByTransfusionCenterId(transfusionCenterId);
    }

    public List<DonationHistory> getCompletedAppointments(long donorId) {
        return appointmentRepository.findAllByDonorIdAndStatusOrderByTimeDesc(donorId, AppointmentStatus.COMPLETED)
                .stream()
                .map(a -> new DonationHistory(
                        a.getId(),
                        a.getTime(),
                        a.getTransfusionCenter().getName() + " - " + a.getTransfusionCenter().getAddress().getCity().getName()
                ))
                .toList();
    }
    
    private void setAppointmentDetails(Appointment appointment, AppointmentRequest request) {
        if (request.time().isBefore(OffsetDateTime.now())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "appointment_invalid_time");
        }

        Donor donor = donorRepository
                .findById(request.userId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "donor_not_found"));

        TransfusionCenter transfusionCenter = transfusionCenterRepository
                .findById(request.transfusionCenterId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "transfusion_center_not_found"));
        
        appointment.setDonor(donor);
        appointment.setTransfusionCenter(transfusionCenter);
        appointment.setTime(request.time());
    }
    
    private Appointment getAppointment(long appointmentId) {
        return appointmentRepository
                .findById(appointmentId)
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "appointment_not_found"));
    }
}
