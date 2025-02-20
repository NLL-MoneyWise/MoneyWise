import { http, HttpResponse } from 'msw';

export const handlers = [
    http.post('/api/auth/login', async ({ request }) => {
        const data = await request.json();

        if (!data.email || !data.password) {
            return new HttpResponse(
                JSON.stringify({
                    message: '이메일과 비밀번호를 입력해주세요.',
                    typeName: 'VALIDATION_ERROR'
                }),
                { status: 400 }
            );
        }

        if (
            data.email === 'test@naver.com' &&
            data.password === 'password123'
        ) {
            return HttpResponse.json(
                {
                    accessToken:
                        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsIm5hbWUiOiLthYzsiqTtirgiLCJuaWNrTmFtZSI6Iu2FjOyKpO2KuOuLieuEpOyehCIsImV4cCI6MTczOTEwODQ4MH0.ZaMBUEGyEf0vs2ebstGvIiBH0AhE2eWLBacn7Ex1TRA',
                    message: '반갑습니다 테스트님'
                },
                { status: 200 }
            );
        }

        return new HttpResponse(
            JSON.stringify({
                message: '이메일 또는 비밀번호가 일치하지 않습니다.',
                typeName: 'VALIDATION_ERROR'
            }),
            { status: 401 }
        );
    }),
    http.post('/api/auth/signup', async ({ request }) => {
        const data = await request.json();

        // 필요 입력데이터 검증
        if (!data.email || !data.password || !data.nickname || !data.name) {
            return new HttpResponse(
                JSON.stringify({
                    message: '필요 정보를 입력해주세요.',
                    typeName: 'VALIDATION_ERROR'
                }),
                { status: 400 }
            );
        }

        // 이미 존재하는 이메일 검증
        if (data.email === 'test@naver.com') {
            return new HttpResponse(
                JSON.stringify({
                    message: '이미 가입된 이메일입니다.',
                    typeName: 'VALIDATION_ERROR'
                }),
                { status: 409 }
            );
        }

        return new HttpResponse(
            JSON.stringify({
                message: '회원가입에 성공했습니다.'
            }),
            { status: 200 }
        );
    })
];
