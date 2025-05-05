'use server';
import {
    LoginRequest,
    KakaoLoginRequest,
    SignUpRequest,
    ValidateRequest
} from './../types/request/index';
import {
    LoginResponse,
    SignUpResponse,
    ValidateResponse,
    RefreshValidateResponse
} from './../types/reponse/index';
import { saveAccessToken } from '../util/toekn';
import { AuthRepositoryImpl } from '../util/respository';

const authRepository = AuthRepositoryImpl.getInstance();

export async function login(credentials: LoginRequest): Promise<LoginResponse> {
    const data = await authRepository.login(credentials);

    await saveAccessToken(data.access_token);

    return data;
}

export async function kakaoLogin(
    credentials: KakaoLoginRequest
): Promise<LoginResponse> {
    const data = await authRepository.kakaoLogin(credentials);

    await saveAccessToken(data.access_token);

    return data;
}

export async function signUp(userData: SignUpRequest): Promise<SignUpResponse> {
    const data = await authRepository.signUp(userData);

    return data;
}

export async function validate(
    token: ValidateRequest
): Promise<ValidateResponse> {
    const data = await authRepository.validate(token);

    return data;
}

export async function refresh(): Promise<RefreshValidateResponse> {
    const data = await authRepository.refresh();

    return data;
}
