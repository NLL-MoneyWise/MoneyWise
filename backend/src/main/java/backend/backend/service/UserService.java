package backend.backend.service;

import backend.backend.common.ErrorType;
import backend.backend.domain.User;
import backend.backend.dto.response.ErrorResponse;
import backend.backend.exception.LoginException;
import backend.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new LoginException(new ErrorResponse("ERROR", ErrorType.AUTHENTICATION_ERROR, "이메일을 찾을 수 없습니다.")));
    }
}
