package backend.backend.Repository;

import backend.backend.dto.consumption.model.ByCategory;
import backend.backend.dto.consumption.model.TopExpense;
import backend.backend.repository.ConsumptionRepository;
import backend.backend.repository.ConsumptionRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ConsumptionRepositoryTest {
    @Autowired
    private ConsumptionRepository consumptionRepository;

    @Test
    void findByCategoryAndEmailTest() {
        String email = "test@naver.com";
        List<ByCategory> result = consumptionRepository.findByCategoryAndEmail(email, null, null, null);
        for (int i = 0; i < result.size(); i++) {
            System.out.println("Category Result: " + result.get(i).getName() + ", " + result.get(i).getAmount());
        }
    }

    @Test
    void findTopExpenseByEmailTest() {
        String email = "test@naver.com";
        List<TopExpense> result = consumptionRepository.findTopExpenseByEmail(email, null, null, null);
        for (int i = 0; i < result.size(); i++) {
            System.out.println("Category Result: " + result.get(i).getName() + ", " + result.get(i).getAmount());
        }
    }
}
