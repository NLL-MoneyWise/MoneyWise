'use client';
import Button from '@/app/common/components/Button/Button';
import InputField from '@/app/common/components/Input/InputField';
import React, { useState } from 'react';
import useValidateForm from '../../hooks/useValidateForm';

const SignUpForm = () => {
    const [email, setEmail] = useState<string>('');
    const [password, setPassword] = useState<string>('');
    const [confirmPassword, setConfirmPassword] = useState<string>('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        validateForm({ email, password, confirmPassword });
    };

    const { getFieldError, hasFieldError, validateForm } = useValidateForm();

    return (
        <form className="w-[80%] m-auto" onSubmit={handleSubmit}>
            <div className="flex items-end justify-center ">
                <div className="w-4/5 mr-3">
                    <InputField
                        element="input"
                        placeholder="이메일을 입력해주세요"
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        isError={hasFieldError('email')}
                        label={getFieldError('email')}
                        aria-required="true"
                        aria-label="이메일 입력"
                    />
                </div>
                <div className="w-1/5">
                    <Button height={42}>{'확인'}</Button>
                </div>
            </div>

            <div className="mt-4" />
            <InputField
                element="input"
                placeholder="비밀번호를 입력해주세요"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                isError={hasFieldError('confirmPassword')}
                label={getFieldError('confirmPassword')}
                aria-required="true"
                aria-label="비밀번호 입력"
            />

            <div className="mt-4" />
            <InputField
                element="input"
                placeholder="비밀번호를 확인 해주세요"
                type="text"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                isError={hasFieldError('password')}
                label={getFieldError('password')}
                aria-required="true"
                aria-label="비밀번호 입력"
            />

            <div className="mt-4" />
            <InputField
                element="input"
                placeholder="이름를 입력해주세요"
                type="text"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                isError={false}
                aria-required="true"
                aria-label="이름 입력"
            />

            <div className="mt-4" />
            <InputField
                element="input"
                placeholder="닉네임을 입력해주세요"
                type="text"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                isError={false}
                aria-required="true"
                aria-lable="닉네임 입력"
            />

            <div className="mt-6"></div>
            <Button height={41} type="submit">
                {'회원가입'}
            </Button>
        </form>
    );
};

export default SignUpForm;
