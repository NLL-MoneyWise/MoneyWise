import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

// 추가예정
const protectedRoutes = ['/test'];

export function middleware(request: NextRequest) {
    const accessToken = request.cookies.has('accessToken');
    const path = request.nextUrl.pathname;

    const isProtectedRoute = protectedRoutes.some((route) =>
        path.startsWith(route)
    );

    if (isProtectedRoute && !accessToken) {
        const response = NextResponse.redirect(
            new URL(`/login?callbackUrl=${path}`, request.url)
        );
        return response;
    }

    return NextResponse.next();
}

export const config = {
    matcher: ['/test']
};
