'use client';

import { useToastStore } from '@/app/common/hooks/useToastStore';
import { AuthRepositoryImpl } from '../respository';
import { useMutation } from '@tanstack/react-query';
import { useRouter } from 'next/navigation';

const useAuthMutation = () => {
    const { addToast } = useToastStore();
    const authRepository = AuthRepositoryImpl.getInstance();
    const router = useRouter();

    const signUpMutation = useMutation({
        mutationFn: authRepository.signUp.bind(authRepository),
        onSuccess: () => {
            addToast('회원가입에 성공했습니다.', 'success');
            router.push('/login');
        }
    });

    return {
        signUpMutation
    };
};

export default useAuthMutation;
