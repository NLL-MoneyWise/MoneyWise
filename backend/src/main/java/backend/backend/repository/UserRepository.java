package backend.backend.repository;

import backend.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByProviderAndProviderId(String provider, Long providerId);
    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByProviderId(Long kakaoId);

    @Modifying
    @Query("UPDATE User u SET u.income = :income WHERE u.email = :email")
    int updateIncomeByEmail(@Param("email") String email, @Param("income") Long income);

    @Modifying
    @Query("UPDATE User u SET u.fixed_cost = :fixed_cost WHERE u.email = :email")
    int updateFixedCostByEmail(@Param("email") String email, @Param("fixed_cost") Long fixed_cost);
}