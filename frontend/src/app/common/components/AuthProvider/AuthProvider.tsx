'use client';
import { createContext, useContext } from 'react';

interface AuthContextType {
    isLoggedIn: boolean;
}

const AuthContext = createContext<AuthContextType>({ isLoggedIn: false });

export const useAuth = () => useContext(AuthContext);

export default function AuthProvider({
    children,
    isLoggedIn
}: {
    children: React.ReactNode;
    isLoggedIn: boolean;
}) {
    return (
        <AuthContext.Provider value={{ isLoggedIn }}>
            {children}
        </AuthContext.Provider>
    );
}
