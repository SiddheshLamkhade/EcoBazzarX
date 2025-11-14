package EcoBazaarX.repository;

import EcoBazaarX.entity.CarbonInsight;
import EcoBazaarX.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarbonInsightRepository extends JpaRepository<CarbonInsight, Long> {
    List<CarbonInsight> findByUserOrderByRecordedAtDesc(User user);
}
