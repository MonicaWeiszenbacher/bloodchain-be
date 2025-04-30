package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.TransfusionCenter;

@Repository
public interface TransfusionCenterRepository extends CrudRepository<TransfusionCenter, Long> {
}
