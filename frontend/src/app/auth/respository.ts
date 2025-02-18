import { defaultApi } from './../common/util/api';
import { AxiosInstance } from 'axios';
import { LoginRequest } from './types/request/index';
import { LoginResponse } from './types/reponse/response-login';

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
}
