'use client';
import React, { useRef } from 'react';
import Button from '@/app/common/components/Button/Button';
import InputField from '@/app/common/components/Input/InputField';
import Link from 'next/link';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import Text from '@/app/common/components/Text/Text';
import { login } from '../../api/action';
import { useUserStore } from '@/stores/userStore';
import { useRouter } from 'next/navigation';

const LoginForm = () => {
    const emailRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);
    const { setUser } = useUserStore();
    const { addToast } = useToastStore();
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        try {
            e.preventDefault();

            const email = emailRef.current!.value.trim();
            const password = passwordRef.current!.value.trim();

            //  아이디와 패스워드 입력 요구
            if (!email || !password) {
                addToast('아이디와 비밀번호를 입력해주세요', 'error');
                return;
            }

            const loginData = {
                email: email,
                password: password
            };

            const response = await login(loginData);

            // 유저 정보 로컬 저장
            setUser({ email: response.email, nickName: response.nickname });

            // 갈려하던 파라미터를 불러와 처리
            const searchParams = new URLSearchParams(window.location.search);
            const callbackUrl = searchParams.get('callbackUrl') || '/';

            router.push(callbackUrl);

            addToast(response.message, 'success');
        } catch (err) {
            addToast('로그인에 실패했습니다.', 'success');
        }
    };

    const handleKakaoLoginClick = () => {
        const kakaoUrl = process.env.NEXT_PUBLIC_KAKAO_URL;

        if (!kakaoUrl) {
            addToast('현재 카카오톡 로그인은 사용할 수 없습니다.', 'error');
            return;
        }
        window.location.href = kakaoUrl;
    };

    return (
        <form className="w-[75%] m-auto" onSubmit={handleSubmit}>
            <InputField
                element="input"
                placeholder="이메일을 입력해주세요"
                type="email"
                ref={emailRef}
                aria-required="true"
                aria-label="이메일 입력"
            />
            <div className="mt-7" />
            <InputField
                element="input"
                placeholder="비밀번호를 입력해주세요"
                type="password"
                ref={passwordRef}
                aria-required="true"
                aria-label="비밀번호 입력"
            />
            <div className="mt-7" />
            <Button type="submit" className="h-12 w-full text-xl">
                {'로그인'}
            </Button>
            <div className="mt-4" />

            <Button
                variant="kakao"
                type="button"
                className="h-12 w-full text-xl"
                onClick={handleKakaoLoginClick}
            >
                카카오 로그인
            </Button>

            <div className="mt-4" />
            <Link
                href={'/join'}
                className="flex justify-center items-center  text-gray-500 "
            >
                <Text>{'회원 가입'}</Text>
            </Link>
        </form>
    );
};

export default LoginForm;
