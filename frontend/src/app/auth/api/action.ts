import {
    LoginRequest,
    KakaoLoginRequest,
    SignUpRequest
} from './../types/request/index';
import { LoginResponse, SignUpResponse } from './../types/reponse/index';
import { AuthRepositoryimpl } from '../respository';
import { saveAccessToken } from '../util/toekn';
import { CustomError, ErrorType } from '@/app/common/types/error/error';

const authRepository = new AuthRepositoryimpl();

export async function login(credentials: LoginRequest): Promise<LoginResponse> {
    try {
        const data = await authRepository.login(credentials);

        saveAccessToken(data.access_token);

        return data;
    } catch (err) {
        throw new CustomError(
            '로그인 처리 중 오류가 발생했습니다',
            400,
            ErrorType.NETWORK
        );
    }
}

export async function kakaoLogin(
    credentials: KakaoLoginRequest
): Promise<LoginResponse> {
    try {
        const data = await authRepository.kakaoLogin(credentials);

        saveAccessToken(data.access_token);

        return data;
    } catch (err) {
        throw new CustomError(
            '카카오 로그인 중 오류가 발생했습니다',
            400,
            ErrorType.NETWORK
        );
    }
}

export async function signUp(
    credentials: SignUpRequest
): Promise<SignUpResponse> {
    try {
        const data = await authRepository.signUp(credentials);

        return data;
    } catch (err) {
        throw new CustomError(
            '회원가입 중 에러가 발생했습니다',
            400,
            ErrorType.NETWORK
        );
    }
}
