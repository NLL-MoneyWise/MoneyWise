package backend.backend.Repository;

import backend.backend.domain.User;
import backend.backend.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void 사용자_저장_테스트() {
        User user = new User();
        user.setEmail("test@email.com");
        user.setNickname("test_nickname");
        user.setPassword("password");
        user.setName("test");

        User saveUser = userRepository.save(user);
        Assertions.assertThat(saveUser.getEmail()).isEqualTo("test@email.com");
    }
}
