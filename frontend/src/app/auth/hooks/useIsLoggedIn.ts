'use client';
import { useEffect, useState } from 'react';

const useIsLoggedIn = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                // API 라우트를 통해 로그인 상태 확인
                const response = await fetch('/api/auth/check');
                const data = await response.json();
                setIsLoggedIn(data.isLoggedIn);
            } catch (error) {
                console.error('로그인 상태 확인 중 오류:', error);
                setIsLoggedIn(false);
            } finally {
                setLoading(false);
            }
        };

        checkLoginStatus();
    }, []);

    return { isLoggedIn, loading };
};

export default useIsLoggedIn;
