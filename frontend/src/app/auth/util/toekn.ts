'use server';
// utils/tokenUtils.ts
import { cookies } from 'next/headers';

// 액세스 토큰 저장 유틸 함수

export const saveAccessToken = async (token: string) => {
    const cookieStore = await cookies();

    cookieStore.set('accessToken', token, {
        httpOnly: true,
        secure: process.env.NODE_ENV === 'production',
        sameSite: 'lax',
        maxAge: 3600,
        path: '/'
    });
};

// 액세스 토큰 가져오는 함수
export const getAccessToken = async () => {
    const cookieStore = await cookies();

    const token = cookieStore.get('accessToken');

    if (!token) return null;

    return token.value;
};

// 액세스 토큰 삭제하는 함수
export const removeAccessToken = async () => {
    const cookieStore = await cookies();

    cookieStore.delete('accessToken');
};
