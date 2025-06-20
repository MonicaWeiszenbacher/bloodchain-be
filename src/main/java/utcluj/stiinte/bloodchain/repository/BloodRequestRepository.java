package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.donation.BloodRequest;

import java.util.List;

@Repository
public interface BloodRequestRepository extends CrudRepository<BloodRequest, Long> {

    List<BloodRequest> findBloodRequestByTransfusionCenterId(long transfusionCenterId);
}
