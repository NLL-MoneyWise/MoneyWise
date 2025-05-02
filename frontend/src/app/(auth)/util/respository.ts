import { AxiosInstance } from 'axios';
import {
    LoginRequest,
    SignUpRequest,
    KakaoLoginRequest,
    ValidateRequest
} from '../types/request/index';
import {
    LoginResponse,
    ValidateResponse,
    SignUpResponse,
    RefreshValidateResponse
} from '../types/reponse/index';
import { defaultApi } from '@/app/common/util/api';

interface AuthRepository {
    login(credentials: LoginRequest): Promise<LoginResponse>;
    kakaoLogin(credentials: KakaoLoginRequest): Promise<LoginResponse>;
    signUp(userData: SignUpRequest): Promise<SignUpResponse>;
    validate({ access_token }: ValidateRequest): Promise<ValidateResponse>;
    refresh(): Promise<RefreshValidateResponse>;
}

export class AuthRepositoryImpl implements AuthRepository {
    private static instance: AuthRepositoryImpl | null = null;
    private api: AxiosInstance;

    private constructor() {
        this.api = defaultApi();
    }

    public static getInstance(): AuthRepositoryImpl {
        if (!AuthRepositoryImpl.instance) {
            AuthRepositoryImpl.instance = new AuthRepositoryImpl();
        }
        return AuthRepositoryImpl.instance;
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

    async validate({
        access_token
    }: ValidateRequest): Promise<ValidateResponse> {
        const { data } = await this.api.post('/auth/validate', access_token);
        return data;
    }

    async refresh(): Promise<RefreshValidateResponse> {
        const { data } = await this.api.post('/auth/refresh');
        return data;
    }
}
