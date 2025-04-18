package backend.backend.service;

import backend.backend.dto.auth.request.BaseLoginRequest;
import backend.backend.dto.auth.request.BaseSignupRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthService<L extends BaseLoginRequest, S extends BaseSignupRequest> {
    public String login(L request);

    public void signup(S request);
}
