'use client';
import React, { useRef } from 'react';
import Button from '@/app/common/components/Button/Button';
import InputField from '@/app/common/components/Input/InputField';
import useAuthMutation from '../../hooks/useAuthMutation';
import Link from 'next/link';

const LoginForm = () => {
    const emailRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);

    const { loginMutation } = useAuthMutation();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const loginData = {
            email: emailRef.current!.value.trim(),
            password: passwordRef.current!.value.trim()
        };

        loginMutation.mutate(loginData);
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
            <div className="mt-7"></div>
            <InputField
                element="input"
                placeholder="비밀번호를 입력해주세요"
                type="password"
                ref={passwordRef}
                aria-required="true"
                aria-label="비밀번호 입력"
            />
            <div className="mt-7"></div>
            <Button height={46} type="submit">
                {'로그인'}
            </Button>
            <div className="mt-1"></div>
            <Link
                href={'/join'}
                className="flex justify-center items-center text-xl text-gray-500"
            >
                {'회원 가입'}
            </Link>
        </form>
    );
};

export default LoginForm;
