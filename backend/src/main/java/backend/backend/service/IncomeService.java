package backend.backend.service;

import backend.backend.domain.income.Income;
import backend.backend.domain.income.primaryKey.IncomeId;
import backend.backend.dto.income.model.IncomeDTO;
import backend.backend.dto.income.request.IncomeSaveAndUpdateRequest;
import backend.backend.dto.income.response.*;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.exception.ValidationException;
import backend.backend.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public IncomeSaveAndUpdateResponse save(String email, IncomeSaveAndUpdateRequest request) {
        try {
            if (request.getDay() < 1 || request.getDay() > 31) {
                throw new ValidationException("잘못된 day 값 입니다.");
            }

            IncomeId id = new IncomeId();
            Income income = new Income();

            id.setDay(request.getDay());
            id.setEmail(email);

            income.setCost(request.getCost());
            income.setId(id);

            incomeRepository.save(income);

            return new IncomeSaveAndUpdateResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("소득 저장에 실패했습니다.");
        }
    }

    public IncomeFindAllResponse findAll(String email) {
        try {
            List<Income> incomes = incomeRepository.findById_Email(email);
            List<IncomeDTO> incomeDTOList = new ArrayList<>();

            for (Income income : incomes) {
                IncomeDTO incomeDTO = new IncomeDTO();

                incomeDTO.setCost(income.getCost());
                incomeDTO.setDay(income.getId().getDay());

                incomeDTOList.add(incomeDTO);
            }

            IncomeFindAllResponse response = new IncomeFindAllResponse();
            response.setIncomeDTOList(incomeDTOList);

            return response;
        } catch (DataAccessException e) {
            throw new DatabaseException("조회에 실패했습니다.");
        }
    }

    public IncomeFindOneResponse findOne(String email, Long day) {
        try {
            if (day < 1 || day > 31) {
                throw new ValidationException("잘못된 day 값 입니다.");
            }

            IncomeId id = new IncomeId();
            id.setDay(day);
            id.setEmail(email);

            Income income = incomeRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("해당하는 소득액을 찾을 수 없습니다."));

            IncomeFindOneResponse response = new IncomeFindOneResponse();
            response.setCost(income.getCost());

            return response;
        } catch (DataAccessException e) {
            throw new DatabaseException("조회에 실패했습니다.");
        }
    }

    public IncomeDeleteAllResponse deleteAll(String email) {
        try {
            incomeRepository.deleteById_Email(email);

            return new IncomeDeleteAllResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("삭제에 실패했습니다.");
        }
    }

    public IncomeDeleteOneResponse deleteOne(String email, Long day) {
        try {
            if (day < 1 || day > 31) {
                throw new ValidationException("잘못된 day 값 입니다.");
            }

            IncomeId id = new IncomeId();
            id.setEmail(email);
            id.setDay(day);

            incomeRepository.deleteById(id);

            return new IncomeDeleteOneResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("삭제에 실패했습니다.");
        }
    }
}
