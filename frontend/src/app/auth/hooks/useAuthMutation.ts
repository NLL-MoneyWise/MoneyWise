'use client';
import { LoginResponse } from './../types/reponse/response-login';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { AuthRepositoryimpl } from '../respository';
import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';

import { useUserStore } from '@/stores/userStore';

const useAuthMutation = () => {
    const { addToast } = useToastStore();
    const authRepository = new AuthRepositoryimpl();
    const router = useRouter();
    const { setUser } = useUserStore();

    const loginMutation = useMutation({
        mutationFn: authRepository.login.bind(authRepository),
        onSuccess: async (response: LoginResponse) => {
            const { message, access_token, email, nickname } = response;

            // 유저 정보 로컬 저장
            setUser({ email, nickName: nickname });

            // 갈려하던 파라미터를 불러와 처리
            const searchParams = new URLSearchParams(window.location.search);
            const callbackUrl = searchParams.get('callbackUrl') || '/';

            router.push(callbackUrl);

            addToast(message, 'success');
        }
    });

    const signUpMutation = useMutation({
        mutationFn: authRepository.signUp.bind(authRepository),
        onSuccess: () => {
            addToast('회원가입에 성공했습니다.', 'success');
            router.push('/login');
        }
    });

    const kakaoLogin = useMutation({
        mutationFn: authRepository.kakaoLogin.bind(authRepository),
        onSuccess: (reponse: LoginResponse) => {
            const { message } = reponse;
            addToast(message, 'success');
        },
        onError: () => {
            router.push('/login-failed');
        }
    });

    return {
        loginMutation,
        signUpMutation,
        kakaoLogin
    };
};

export default useAuthMutation;
