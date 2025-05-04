'use server';
import { checkAccessToken } from '@/app/(auth)/util/toekn';
import { Suspense } from 'react';
import AuthProvider from '../AuthProvider/AuthProvider';

interface AuthWrapperProps {
    children: React.ReactNode;
}

const AuthWrapper = async ({ children }: AuthWrapperProps) => {
    const isLoggedIn = await checkAccessToken();

    return (
        <Suspense fallback={<div>로딩 중..</div>}>
            <AuthProvider isLoggedIn={isLoggedIn}>{children}</AuthProvider>
        </Suspense>
    );
};

export default AuthWrapper;
