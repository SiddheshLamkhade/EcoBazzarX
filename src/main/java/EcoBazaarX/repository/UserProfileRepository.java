package EcoBazaarX.repository;

import EcoBazaarX.entity.UserProfile;
import EcoBazaarX.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(User user);
    List<UserProfile> findAllByOrderByCarbonPointsDesc();
    List<UserProfile> findAllByOrderByCarbonSavedDesc();
}
