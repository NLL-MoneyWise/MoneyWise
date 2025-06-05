'use server';
import { checkAccessToken, getAccessToken } from '@/app/(auth)/util/toekn';

import AuthProvider from '../AuthProvider/AuthProvider';

interface AuthWrapperProps {
    children: React.ReactNode;
}

const AuthWrapper = async ({ children }: AuthWrapperProps) => {
    const isLoggedIn = await checkAccessToken();
    const accessUrl = await getAccessToken();
    return (
        <AuthProvider isLoggedIn={isLoggedIn} accessUrl={accessUrl}>
            {children}
        </AuthProvider>
    );
};

export default AuthWrapper;
