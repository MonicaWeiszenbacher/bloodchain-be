package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.Appointment;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {
}
