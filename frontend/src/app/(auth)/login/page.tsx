'use client';
import LoginForm from '@/app/auth/components/LoginForm/LoginForm';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { useUserStore } from '@/stores/userStore';
import { useSearchParams } from 'next/navigation';
import { useEffect } from 'react';

export default function LoginPage() {
    const searchParams = useSearchParams();
    const { addToast } = useToastStore();
    const { logout } = useUserStore();

    useEffect(() => {
        const error = searchParams.get('error');
        if (error === 'token_expired') {
            addToast(
                '로그인 세션이 만료되었습니다. 다시 로그인해주세요.',
                'warning'
            );
            logout();
        }
    }, [searchParams]);

    return (
        <>
            <LoginForm />
        </>
    );
}
