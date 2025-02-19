'use client';
import React, { useRef } from 'react';
import Button from '@/app/common/components/Button/Button';
import InputField from '@/app/common/components/Input/InputField';
import useAuthMutation from '../../hooks/useAuthMutation';

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
        <form className="w-[80%] m-auto" onSubmit={handleSubmit}>
            <InputField
                element="input"
                placeholder="이메일을 입력해주세요"
                type="text"
                ref={emailRef}
                isError={false}
            />
            <div className="mt-2"></div>
            <InputField
                element="input"
                placeholder="비밀번호를 입력해주세요"
                type="text"
                ref={passwordRef}
                isError={false}
            />
            <div className="mt-6"></div>
            <Button width={320} height={41} type="submit">
                {'로그인'}
            </Button>
        </form>
    );
};

export default LoginForm;
