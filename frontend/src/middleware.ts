import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import { validate, refresh } from './app/auth/api/action';
import {
    getAccessToken,
    removeAccessToken,
    saveAccessToken
} from './app/auth/util/toekn';

// 추가예정
const protectedRoutes = ['/calendar', '/upload', '/history'];

export async function middleware(request: NextRequest) {
    const token = await getAccessToken();
    const path = request.nextUrl.pathname;

    const isProtectedRoute = protectedRoutes.some((route) =>
        path.startsWith(route)
    );

    // 보호된 경로인데 토큰이 없으면 로그인 페이지로 리다이렉트
    if (isProtectedRoute && !token) {
        return NextResponse.redirect(
            new URL(`/login?callbackUrl=${path}`, request.url)
        );
    }
}

export const config = {
    matcher: ['/calendar', '/upload', '/history']
};
