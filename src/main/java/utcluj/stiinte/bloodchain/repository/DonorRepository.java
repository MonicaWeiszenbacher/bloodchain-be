package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.Donor;

@Repository
public interface DonorRepository extends CrudRepository<Donor, Long> {
}
