import { TokenType } from './../../../auth/types/token/index';
import { NextResponse } from 'next/server';
import { cookies } from 'next/headers';

export async function GET(request: Request) {
    try {
        const cookieStore = await cookies();

        // URL에서 tokenType 파라미터 가져오기
        const { searchParams } = new URL(request.url);
        const tokenType = searchParams.get('type') as TokenType;

        const Token = cookieStore.get(tokenType);

        if (!Token) {
            return NextResponse.json(
                {
                    success: false,
                    message: '토큰이 존재하지 않습니다.'
                },
                { status: 401 }
            );
        }

        return NextResponse.json({
            success: true,
            Token: Token.value
        });
    } catch (error) {
        console.error(error);

        return NextResponse.json(
            { success: false, message: '쿠키 설정 실패' },
            { status: 400 }
        );
    }
}

export async function POST(req: Request) {
    try {
        const { accessToken, refreshToken } = await req.json();

        const response = NextResponse.json({
            success: true,
            message: '로그인 성공'
        });

        response.cookies.set({
            name: 'accessToken',
            value: accessToken,
            httpOnly: true,
            secure: process.env.NODE_ENV === 'production',
            sameSite: 'strict',
            maxAge: 60 * 60
        });

        response.cookies.set({
            name: 'refreshToken',
            value: refreshToken,
            httpOnly: true,
            secure: process.env.NODE_ENV === 'production',
            sameSite: 'strict',
            maxAge: 60 * 60 * 24 * 7
        });

        return response;
    } catch (error) {
        console.error(error);

        return NextResponse.json(
            { success: false, message: '쿠키 설정 실패' },
            { status: 400 }
        );
    }
}

export async function DELETE() {
    try {
        const cookieStore = await cookies();

        const response = NextResponse.json({
            success: true,
            message: '토큰이 성공적으로 삭제되었습니다.'
        });

        if (cookieStore.has('accessToken')) {
            response.cookies.delete('accessToken');
        }
        if (cookieStore.has('refreshToken')) {
            response.cookies.delete('refreshToken');
        }

        return response;
    } catch (error) {
        console.error(error);
        return NextResponse.json(
            { success: false, message: '쿠키 설정 실패' },
            { status: 400 }
        );
    }
}
