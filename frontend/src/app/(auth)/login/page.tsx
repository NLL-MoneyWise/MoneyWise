'use client';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { useUserStore } from '@/stores/userStore';
import { useSearchParams } from 'next/navigation';
import { useEffect } from 'react';
import LoginForm from '../components/LoginForm/LoginForm';

export default function LoginPage() {
    const searchParams = useSearchParams();
    const { addToast } = useToastStore();
    const { clearUser } = useUserStore();

    useEffect(() => {
        const error = searchParams.get('error');
        if (error === 'token_expired') {
            addToast('세션이 만료되었습니다.', 'warning');
            clearUser();
        }
    }, []);

    return (
        <>
            <LoginForm />
        </>
    );
}
