package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.donation.PeriodicDonation;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodicDonationRepository extends CrudRepository<PeriodicDonation, Long> {
    
    List<PeriodicDonation> findAllByNotificationSentIsFalse();
    
    Optional<PeriodicDonation> findByDonorId(long id);

}