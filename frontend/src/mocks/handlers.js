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
    })
];
