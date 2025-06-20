package utcluj.stiinte.bloodchain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utcluj.stiinte.bloodchain.data.transfusioncenter.TransfusionCenterData;
import utcluj.stiinte.bloodchain.model.user.TransfusionCenter;

import java.util.List;

@Repository
public interface TransfusionCenterRepository extends CrudRepository<TransfusionCenter, Long> {
    
    @Query(
            value = """
                    SELECT \
                    tc.id AS id, \
                    tc.name AS name, \
                    c_tc.name AS cityName \
                    FROM transfusion_center tc \
                    JOIN app_user au_tc ON tc.id = au_tc.id \
                    JOIN city c_tc ON au_tc.city_id = c_tc.id, \
                    app_user au_user \
                    JOIN city uc ON au_user.city_id = uc.id \
                    WHERE au_user.id = :userId \
                    ORDER BY \
                    (6371 * ACOS( \
                    COS(uc.latitude * PI()/180) * COS(c_tc.latitude * PI()/180) * \
                    COS(c_tc.longitude * PI()/180 - uc.longitude * PI()/180) + \
                    SIN(uc.latitude * PI()/180) * SIN(c_tc.latitude * PI()/180) \
                    )) ASC \
                    LIMIT 10""",
            nativeQuery = true)
    List<TransfusionCenterData> findTop10ClosestTransfusionCenters(@Param("donorId") Long userId);
}
