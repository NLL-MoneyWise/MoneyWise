'use client';
import { LoginResponse } from './../types/reponse/response-login';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { AuthRepositoryimpl } from '../respository';
import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';
import saveToekn from '../util/saveToekn';
import { useUserStore } from '@/stores/userStore';

const useAuthMutation = () => {
    const { addToast } = useToastStore();
    const authRepository = new AuthRepositoryimpl();
    const router = useRouter();
    const { setUser } = useUserStore();

    const loginMutation = useMutation({
        mutationFn: authRepository.login.bind(authRepository),
        onSuccess: async (response: LoginResponse) => {
            const { message, accessToken, email, nickName } = response;
            // 토큰 저장
            await saveToekn(accessToken);

            // 유저 정보 로컬 저장
            setUser({ email, nickName });

            // 갈려하던 파라미터를 불러와 처리
            const searchParams = new URLSearchParams(window.location.search);
            const callbackUrl = searchParams.get('callbackUrl') || '/';

            addToast(message, 'success');
            router.push(callbackUrl);
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
        mutationFn: authRepository.kakoLogin.bind(authRepository),
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
