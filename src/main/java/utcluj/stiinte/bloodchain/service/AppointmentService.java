package utcluj.stiinte.bloodchain.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utcluj.stiinte.bloodchain.data.appointment.AppointmentData;
import utcluj.stiinte.bloodchain.exception.AppException;
import utcluj.stiinte.bloodchain.model.Appointment;
import utcluj.stiinte.bloodchain.model.Donor;
import utcluj.stiinte.bloodchain.model.TransfusionCenter;
import utcluj.stiinte.bloodchain.repository.AppointmentRepository;
import utcluj.stiinte.bloodchain.repository.DonorRepository;
import utcluj.stiinte.bloodchain.repository.TransfusionCenterRepository;

import java.time.OffsetDateTime;

@Service
@AllArgsConstructor
public class AppointmentService {
    
    private final AppointmentRepository appointmentRepository;
    private final DonorRepository donorRepository;
    private final TransfusionCenterRepository transfusionCenterRepository;

    @Transactional
    public void saveAppointment(AppointmentData appointment) {
        if (appointment.time().isBefore(OffsetDateTime.now())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "appointment_invalid_time");
        }
        
        Donor donor = donorRepository
                .findById(appointment.userId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "donor_not_found"));
        
        TransfusionCenter transfusionCenter = transfusionCenterRepository
                .findById(appointment.transfusionCenterId())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "transfusion_center_not_found"));

        Appointment newAppointment = new Appointment();
        newAppointment.setDonor(donor);
        newAppointment.setTransfusionCenter(transfusionCenter);
        newAppointment.setTime(appointment.time());
        appointmentRepository.save(newAppointment);
    }
}
