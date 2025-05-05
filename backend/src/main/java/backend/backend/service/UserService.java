package backend.backend.service;

import backend.backend.domain.User;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("이메일을 찾을 수 없습니다."));
    }

    public void incomeUpdate(String email, Long income) {
        try {
            int num = userRepository.updateIncomeByEmail(email, income);
            if(num != 1) {
                throw new DatabaseException("소득액 변경에 실패했습니다.");
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("소득액 변경에 실패했습니다.");
        }
    }

    public void fixedCostUpdate(String email, Long fixed_cost) {
        try {
            int num = userRepository.updateFixedCostByEmail(email, fixed_cost);

            if(num != 1) {
                throw new DatabaseException("고정 지출액 변경에 실패했습니다.");
            }
        } catch (DataAccessException e) {
            throw new DatabaseException("고정 지출액 변경에 실패했습니다.");
        }
     }
}
