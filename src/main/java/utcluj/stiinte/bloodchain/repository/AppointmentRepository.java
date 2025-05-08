package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.Appointment;
import utcluj.stiinte.bloodchain.model.enums.AppointmentStatus;

import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
    
    List<Appointment> findAllByTransfusionCenterId(long id);
    
    List<Appointment> findAllByDonorIdAndStatusOrderByTimeDesc(long id, AppointmentStatus status);
}
