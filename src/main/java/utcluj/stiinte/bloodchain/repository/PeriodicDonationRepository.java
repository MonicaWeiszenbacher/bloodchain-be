package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.PeriodicDonation;

import java.util.List;

@Repository
public interface PeriodicDonationRepository extends CrudRepository<PeriodicDonation, Long> {
    
    List<PeriodicDonation> findAllByNotificationSentIsFalse();

}