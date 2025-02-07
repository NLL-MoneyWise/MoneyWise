'use client';
import React, { useRef } from 'react';
import Button from '@/app/components/common/Button/Button';
import InputField from '@/app/components/common/Input/InputField';

const LoginForm = () => {
    const emailRef = useRef<HTMLInputElement>(null);
    const passwordRef = useRef<HTMLInputElement>(null);

    return (
        <div className="w-[80%] m-auto">
            <InputField
                element="input"
                placeholder="이메일을 입력해주세요"
                label="살려줘"
                type="text"
                ref={emailRef}
            />
            <div className="mt-4"></div>
            <InputField
                element="input"
                placeholder="비밀번호를 입력해주세요"
                label="살려줘"
                type="text"
                ref={passwordRef}
            />
            <div className="mt-6"></div>
            <Button
                width={320}
                height={41}
                handleClick={() => {
                    alert('헬로');
                }}
            >
                {'로그인'}
            </Button>
        </div>
    );
};

export default LoginForm;
