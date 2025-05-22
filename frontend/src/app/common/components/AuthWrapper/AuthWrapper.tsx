'use server';
import { checkAccessToken, getAccessToken } from '@/app/(auth)/util/toekn';
import { Suspense } from 'react';
import AuthProvider from '../AuthProvider/AuthProvider';

interface AuthWrapperProps {
    children: React.ReactNode;
}

const AuthWrapper = async ({ children }: AuthWrapperProps) => {
    const isLoggedIn = await checkAccessToken();
    const accessUrl = await getAccessToken();
    return (
        <Suspense fallback={<div>로딩 중..</div>}>
            <AuthProvider isLoggedIn={isLoggedIn} accessUrl={accessUrl}>
                {children}
            </AuthProvider>
        </Suspense>
    );
};

export default AuthWrapper;
