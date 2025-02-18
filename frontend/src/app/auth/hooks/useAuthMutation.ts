import { LoginResponse } from './../types/reponse/response-login';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { AuthRepositoryimpl } from '../respository';
import { useMutation } from '@tanstack/react-query';
import { useRouter, useSearchParams } from 'next/navigation';
import saveToekn from '../util/saveToekn';
import { useUserStore } from '@/stores/userStore';

const useAuthMutation = () => {
    const { addToast } = useToastStore();
    const authRepository = new AuthRepositoryimpl();
    const router = useRouter();
    const searchParams = useSearchParams();
    const { setUser } = useUserStore();

    const loginMutation = useMutation({
        mutationFn: authRepository.login.bind(authRepository),
        onSuccess: async (response: LoginResponse) => {
            const { message, accessToken, refreshToken, email, nickName } =
                response;
            // 토큰 저장
            await saveToekn(accessToken, refreshToken);
            // 유저 정보 로컬 저장
            setUser({ email, nickName });
            // 갈려하던 파라미터 저장

            const callbackUrl = searchParams.get('callbackUrl') || '/';
            addToast(message, 'success');
            router.push(callbackUrl);
        }
    });

    return {
        loginMutation
    };
};

export default useAuthMutation;
