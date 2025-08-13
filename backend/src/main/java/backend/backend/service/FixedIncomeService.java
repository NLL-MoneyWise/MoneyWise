package backend.backend.service;

import backend.backend.domain.FixedIncome;
import backend.backend.dto.common.response.BaseResponse;
import backend.backend.dto.fixedIncome.common.FixedIncomeDto;
import backend.backend.dto.fixedIncome.request.FixedIncomeSaveRequest;
import backend.backend.dto.fixedIncome.request.FixedIncomeUpdateRequest;
import backend.backend.dto.fixedIncome.response.FixedIncomeFindAllResponse;
import backend.backend.dto.fixedIncome.response.FixedIncomeFindOneResponse;
import backend.backend.exception.AuthException;
import backend.backend.exception.DatabaseException;
import backend.backend.exception.NotFoundException;
import backend.backend.exception.ValidationException;
import backend.backend.repository.FixedIncomeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FixedIncomeService {
    private final FixedIncomeRepository fixedIncomeRepository;

    public BaseResponse create(String email, FixedIncomeSaveRequest request) {
        try {
            LocalDate date = LocalDate.parse(request.getDate());
            LocalDate now = LocalDate.now();

            if (date.getDayOfMonth() > 28) {
                throw new ValidationException("day는 1에서 28 사이의 값을 입력해주세요.");
            }

            if (date.getYear() < now.getYear() || (date.getYear() == now.getYear() && date.getMonthValue() < now.getMonthValue())) {
                throw new ValidationException("현재 월 이전의 날짜는 입력할 수 없습니다.");
            }

            FixedIncome fixedIncome = new FixedIncome();
            fixedIncome.setName(request.getName());
            fixedIncome.setDate(date);
            fixedIncome.setAmount(request.getAmount());
            fixedIncome.setEmail(email);

            fixedIncomeRepository.save(fixedIncome);

            return new BaseResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("저장에 실패했습니다.");
        } catch (DateTimeParseException e) {
            throw new ValidationException("잘못된 날짜 형식입니다.");
        }
    }

    public BaseResponse update(String email, FixedIncomeUpdateRequest request) {
        try {

            LocalDate date = LocalDate.parse(request.getDate());
            LocalDate now = LocalDate.now();

            if (date.getDayOfMonth() > 28) {
                throw new ValidationException("day는 1에서 28 사이의 값을 입력해주세요.");
            }

            if (date.getYear() < now.getYear() || (date.getYear() == now.getYear() && date.getMonthValue() < now.getMonthValue())) {
                throw new ValidationException("현재 월 이전의 날짜는 입력할 수 없습니다.");
            }

            FixedIncome fixedIncome = fixedIncomeRepository.findById(request.getId())
                    .orElseThrow(() -> new NotFoundException("해당하는 고정 소득을 찾을 수 없습니다."));

            if (!fixedIncome.getEmail().equals(email)) {
                throw new AuthException("수정 권한이 없습니다.");
            }

            fixedIncome.setName(request.getName());
            fixedIncome.setDate(date);
            fixedIncome.setAmount(request.getAmount());

            fixedIncomeRepository.save(fixedIncome);

            return new BaseResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("저장에 실패했습니다.");
        } catch (DateTimeParseException e) {
            throw new ValidationException("잘못된 날짜 형식입니다.");
        }
    }

    public FixedIncomeFindOneResponse findOne(String email, Long id) {
        FixedIncome fixedIncome = fixedIncomeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("해당하는 고정 소득을 찾을 수 없습니다."));

        if (!fixedIncome.getEmail().equals(email)) {
            throw new AuthException("접근 권한이 없습니다.");
        }

        FixedIncomeFindOneResponse response = new FixedIncomeFindOneResponse();

        response.setAmount(fixedIncome.getAmount());
        response.setDate(fixedIncome.getDate().toString());
        response.setName(fixedIncome.getName());

        return response;
    }

    public FixedIncomeFindAllResponse findAll(String email) {
        try {
            List<FixedIncome> fixedIncomeList = fixedIncomeRepository.findByEmail(email);

            List<FixedIncomeDto> fixedIncomeDtoList = fixedIncomeList.stream().map(fixedIncome -> {
                FixedIncomeDto fixedIncomeDto = new FixedIncomeDto();
                fixedIncomeDto.setId(fixedIncome.getId());
                fixedIncomeDto.setAmount(fixedIncome.getAmount());
                fixedIncomeDto.setDate(fixedIncome.getDate().toString());
                fixedIncomeDto.setName(fixedIncome.getName());
                return fixedIncomeDto;
            }).toList();

            FixedIncomeFindAllResponse response = new FixedIncomeFindAllResponse();
            response.setFixedIncomeDtoList(fixedIncomeDtoList);

            return response;
        } catch (DataAccessException e) {
            throw new DatabaseException("조회 중 문제가 발생했습니다.");
        }
    }

    public BaseResponse deleteOne(String email, Long id) {
        try {
            FixedIncome fixedIncome = fixedIncomeRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("해당하는 고정 소득을 찾을 수 없습니다."));

            if (!fixedIncome.getEmail().equals(email)) {
                throw new AuthException("접근 권한이 없습니다.");
            }

            fixedIncomeRepository.deleteById(id);

            return new BaseResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("삭제에 실패했습니다.");
        }
    }

    public BaseResponse deleteAll(String email) {
        try {
            fixedIncomeRepository.deleteByEmail(email);

            return new BaseResponse();
        } catch (DataAccessException e) {
            throw new DatabaseException("삭제에 실패했습니다.");
        }
    }
}
