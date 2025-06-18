package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.model.user.TransfusionCenter;
import utcluj.stiinte.bloodchain.data.TransfusionCenterData;

import java.util.List;

@Repository
public interface TransfusionCenterRepository extends CrudRepository<TransfusionCenter, Long> {

    /*@Query(value = "SELECT " +
            "tc.id AS id, " +
            "tc.name AS name, " +
            "c.name AS cityName " +
            "FROM transfusion_center tc " +
            "JOIN app_user au_tc ON tc.id = au_tc.id " + // Join TC to its superclass AppUser
            "JOIN address a_tc ON au_tc.address_id = a_tc.id " + // Get TC's address via its AppUser parent
            "JOIN city c ON a_tc.city_id = c.id, " + // Get TC's city
            "app_user au_user " + // Start a new branch for the querying user
            "JOIN address ua ON au_user.address_id = ua.id " +
            "JOIN city uc ON ua.city_id = uc.id " +
            "WHERE au_user.id = :userId " + // Filter by the user's ID
            "ORDER BY " +
            "(6371 * ACOS( " +
            "COS(uc.latitude * PI()/180) * COS(c.latitude * PI()/180) * " +
            "COS(c.longitude * PI()/180 - uc.longitude * PI()/180) + " +
            "SIN(uc.latitude * PI()/180) * SIN(c.latitude * PI()/180) " +
            ")) ASC " +
            "LIMIT 10",
            nativeQuery = true)*/
    @Query(value = "SELECT " +
            "tc.id AS id, " +
            "tc.name AS name, " +
            "c_tc.name AS cityName " + // Alias for Transfusion Center's City Name
            "FROM transfusion_center tc " +
            "JOIN app_user au_tc ON tc.id = au_tc.id " + // Join TC to its superclass AppUser
            "JOIN city c_tc ON au_tc.city_id = c_tc.id, " + // Get TC's city directly from au_tc
            "app_user au_user " + // Start a new branch for the querying user
            "JOIN city uc ON au_user.city_id = uc.id " + // Get User's city directly from au_user
            "WHERE au_user.id = :userId " + // Filter by the user's ID
            "ORDER BY " +
            "(6371 * ACOS( " +
            "COS(uc.latitude * PI()/180) * COS(c_tc.latitude * PI()/180) * " + // Use c_tc for transfusion center's lat/lon
            "COS(c_tc.longitude * PI()/180 - uc.longitude * PI()/180) + " +
            "SIN(uc.latitude * PI()/180) * SIN(c_tc.latitude * PI()/180) " +
            ")) ASC " +
            "LIMIT 10",
            nativeQuery = true)
    List<TransfusionCenterData> findTop10ClosestTransfusionCenters(@Param("userId") Long userId);
}
