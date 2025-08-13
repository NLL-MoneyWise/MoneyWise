package backend.backend.service;

import backend.backend.domain.income.Income;
import backend.backend.dto.common.response.BaseResponse;
import backend.backend.dto.income.model.IncomeDto;
import backend.backend.dto.income.request.IncomeSaveRequest;
import backend.backend.dto.income.request.IncomeUpdateRequest;
import backend.backend.dto.income.response.IncomeFindAllResponse;
import backend.backend.dto.income.response.IncomeFindOneResponse;
import backend.backend.exception.AuthException;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.exception.ValidationException;
import backend.backend.repository.IncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public BaseResponse create(String email, IncomeSaveRequest request) {
        try {
            LocalDate date = LocalDate.parse(request.getDate());

            Income income = new Income();

            income.setIncome_date(date);
            income.setName(request.getName());
            income.setSortation("변동");
            income.setAmount(request.getAmount());
            income.setEmail(email);

            incomeRepository.save(income);

            return new BaseResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("저장에 실패했습니다.");
        } catch (DateTimeParseException e) {
            throw new ValidationException("잘못된 값이나 형식입니다.");
        }
    }

    public BaseResponse update(String email, IncomeUpdateRequest request) {
        try {
            Income income = incomeRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException("해당하는 소득을 찾을 수 없습니다."));

            if (!income.getEmail().equals(email)) {
                throw new AuthException("수정 권한이 없습니다.");
            }

            LocalDate date = LocalDate.parse(request.getDate());

            income.setIncome_date(date);
            income.setName(request.getName());
            income.setAmount(request.getAmount());

            return new BaseResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("수정에 실패했습니다.");
        } catch (DateTimeParseException e) {
            throw new ValidationException("잘못된 날짜 값이나 형식입니다.");
        }
    }

    public IncomeFindOneResponse findOne(String email, Long id) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당하는 소득을 찾을 수 없습니다."));

        if (!income.getEmail().equals(email)) {
            throw new AuthException("접근 권한이 없습니다.");
        }

        IncomeFindOneResponse response = new IncomeFindOneResponse();

        response.setDate(income.getIncome_date().toString());
        response.setName(income.getName());
        response.setSortation(income.getSortation());
        response.setAmount(income.getAmount());

        return response;
    }

    public IncomeFindAllResponse findAll(String email) {
        try {
            List<Income> incomeList = incomeRepository.findByEmail(email);

            List<IncomeDto> incomeDtoList = incomeList.stream().map(income -> {
                IncomeDto incomeDto = new IncomeDto();

                incomeDto.setSortation(income.getSortation());
                incomeDto.setName(income.getName());
                incomeDto.setAmount(income.getAmount());
                incomeDto.setId(income.getId());
                incomeDto.setDate(income.getIncome_date().toString());

                return incomeDto;
            }).toList();

            IncomeFindAllResponse response = new IncomeFindAllResponse();

            response.setIncomeDtoList(incomeDtoList);

            return response;
        } catch (DataAccessException e) {
            throw new DatabaseException("조회 중 문제가 발생했습니다.");
        }
    }

    public BaseResponse deleteOne(String email, Long id) {
        try {
            Income income = incomeRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("해당하는 소득을 찾을 수 없습니다."));

            if (!income.getEmail().equals(email)) {
                throw new AuthException("접근 권한이 없습니다.");
            }

            incomeRepository.deleteById(id);

            return new BaseResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("삭제에 실패했습니다.");
        }
    }

    public BaseResponse deleteAll(String email) {
        try {
            incomeRepository.deleteByEmail(email);

            return new BaseResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("삭제에 실패했습니다.");
        }
    }

    public
}
