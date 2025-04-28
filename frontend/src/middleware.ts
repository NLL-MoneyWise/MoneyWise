import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';
import { validate, refresh } from './app/auth/api/action';
import {
    getAccessToken,
    removeAccessToken,
    saveAccessToken
} from './app/auth/util/toekn';
import { CustomError, ErrorType } from './app/common/types/error/error';

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

    if (!token) {
        return isProtectedRoute
            ? NextResponse.redirect(
                  new URL(`/login?callbackUrl=${path}`, request.url)
              )
            : NextResponse.next();
    }

    try {
        await validate({ access_token: token });
        return NextResponse.next();
    } catch (err) {
        // 그 외 에러
        if (!(err instanceof CustomError) || err.type !== ErrorType.AUTH) {
            return NextResponse.next();
        }

        try {
            const data = await refresh();
            await saveAccessToken(data.access_token);
            return NextResponse.next();
        } catch (refreshErr) {
            // 토큰 만료 경우
            if (
                refreshErr instanceof CustomError &&
                refreshErr.type === ErrorType.AUTH
            ) {
                await removeAccessToken();
                return NextResponse.redirect(
                    new URL(
                        `/login?callbackUrl=${path}&error=token_expired`,
                        request.url
                    )
                );
            }

            return NextResponse.next();
        }
    }
}

export const config = {
    matcher: ['/calendar', '/upload', '/history']
};
