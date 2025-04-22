import { defaultApi } from './../common/util/api';
import { AxiosInstance } from 'axios';
import {
    LoginRequest,
    SignUpRequest,
    KakaoLoginRequest
} from './types/request/index';
import {
    LoginResponse,
    ReissueResponse,
    SignUpResponse
} from './types/reponse/index';

interface AuthRepository {
    login(credentials: LoginRequest): Promise<LoginResponse>;
}

export class AuthRepositoryimpl implements AuthRepository {
    private api: AxiosInstance;

    constructor() {
        this.api = defaultApi();
    }

    async login(credentials: LoginRequest): Promise<LoginResponse> {
        const { data } = await this.api.post('/auth/login/local', credentials);
        return data;
    }

    async kakaoLogin(credentials: KakaoLoginRequest): Promise<LoginResponse> {
        const { data } = await this.api.post('/auth/login/kakao', credentials);
        return data;
    }

    async signUp(userData: SignUpRequest): Promise<SignUpResponse> {
        const { data } = await this.api.post('/auth/signup', userData);
        return data;
    }

    async reissue(): Promise<ReissueResponse> {
        const { data } = await this.api.post('/auth/refresh');
        return data;
    }
}
