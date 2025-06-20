package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.donation.Donation;
import utcluj.stiinte.bloodchain.model.enums.DonationStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends CrudRepository<Donation, Long> {
    
    List<Donation> findAllByTransfusionCenterIdAndStatusOrderByTimeDesc(long id, DonationStatus status);

    List<Donation> findAllByTransfusionCenterId(long id);
    
    List<Donation> findAllByDonorIdAndStatusOrderByTimeDesc(long id, DonationStatus status);
    
    int countAllByDonorIdAndTokenIsNotNull(long donorId);
    
    Optional<Donation> findTopByDonorIdOrderByTimeDesc(long donorId);
}
