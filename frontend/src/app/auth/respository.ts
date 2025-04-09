import { KakaoLoginRequest } from './types/request/request-login';
import { defaultApi } from './../common/util/api';
import { AxiosInstance } from 'axios';
import { LoginRequest, SignUpRequest } from './types/request/index';
import { LoginResponse } from './types/reponse/response-login';
import { responType } from '../common/types/response/reponse.dto';

interface AuthRepository {
    login(credentials: LoginRequest): Promise<LoginResponse>;
}

export class AuthRepositoryimpl implements AuthRepository {
    private api: AxiosInstance;

    constructor() {
        this.api = defaultApi();
    }

    async login(credentials: LoginRequest): Promise<LoginResponse> {
        const { data } = await this.api.post('/auth/login', credentials);
        return data;
    }

    async kakoLogin(credentials: KakaoLoginRequest): Promise<LoginResponse> {
        const { data } = await this.api.post('/auth/kakao-login', credentials);
        return data;
    }

    async signUp(userData: SignUpRequest): Promise<responType> {
        const { data } = await this.api.post('/auth/signup', userData);
        return data;
    }
}
