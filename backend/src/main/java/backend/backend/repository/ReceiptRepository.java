package backend.backend.repository;

import backend.backend.domain.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, String> {
    List<Receipt> findByEmail(String email);

    @Modifying
    @Query("UPDATE Receipt r SET r.totalAmount = :totalAmount WHERE r.accessUrl = :accessUrl")
    void updateTotalAmountByAccessUrl(String accessUrl, Long totalAmount);
}
