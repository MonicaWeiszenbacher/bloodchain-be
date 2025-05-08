package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.City;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {
}
