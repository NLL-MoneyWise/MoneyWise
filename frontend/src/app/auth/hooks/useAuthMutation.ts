import { useToastStore } from '@/app/common/hooks/useToastStore';
import { AuthRepositoryimpl } from '../respository';
import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';

const useAuthMutation = () => {
    const { addToast } = useToastStore();
    const authRepository = new AuthRepositoryimpl();
    const router = useRouter();

    const loginMutation = useMutation({
        mutationFn: authRepository.login.bind(authRepository),
        onSuccess: () => {
            addToast('로그인에 성공했습니다.', 'success');
            router.push('/');
        }
    });

    const signUpMutation = useMutation({
        mutationFn: authRepository.signUp.bind(authRepository),
        onSuccess: () => {
            addToast('회원가입에 성공했습니다.', 'success');
            router.push('/login');
        }
    });

    return {
        loginMutation,
        signUpMutation
    };
};

export default useAuthMutation;
