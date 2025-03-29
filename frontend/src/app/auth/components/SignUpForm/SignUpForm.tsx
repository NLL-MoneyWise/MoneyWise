'use client';
import Button from '@/app/common/components/Button/Button';
import InputField from '@/app/common/components/Input/InputField';
import React, { useEffect, useState } from 'react';
import useValidateForm from '../../hooks/useValidateForm';
import useAuthMutation from '../../hooks/useAuthMutation';
import { SignUpRequest } from '../../types/request/request-signUp';
import debounce from '@/app/common/util/debounce';

interface User extends SignUpRequest {
    confirmpassword: string;
    [key: string]: string;
}

const UserDataKorean: User = {
    email: '이메일',
    password: '비밀번호',
    confirmpassword: '확인 비밀번호',
    name: '이름',
    nickname: '닉네임'
};

Object.freeze(UserDataKorean);

const checkInputType = (value: string): string => {
    if (value === 'email' || 'password') {
        return value;
    }
    return 'text';
};

const SignUpForm = () => {
    const [userData, setUserData] = useState<User>({
        email: '',
        password: '',
        confirmpassword: '',
        name: '',
        nickname: ''
    });

    const { signUpMutation } = useAuthMutation();
    const { getFieldError, validateForm, isValid } = useValidateForm();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const { ...requestUserData } = userData;
        if (isValid) {
            signUpMutation.mutate(requestUserData);
        }
    };

    useEffect(() => {
        const debouceValidForm = debounce(
            () =>
                validateForm({
                    email: userData.email,
                    password: userData.password,
                    confirmpassword: userData.confirmpassword
                }),
            200
        );
        debouceValidForm();
    }, [userData.email, userData.confirmpassword, userData.password]);

    return (
        <form className="w-[80%] m-auto" onSubmit={handleSubmit}>
            <div className="flex  flex-col">
                {Object.keys(userData).map((val, idx) => (
                    <div key={idx}>
                        <InputField
                            element="input"
                            placeholder={`${UserDataKorean[val]}를 입력해주세요`}
                            type={checkInputType(val)}
                            value={userData[val]}
                            onChange={(e) =>
                                setUserData({
                                    ...userData,
                                    [val]: e.target.value
                                })
                            }
                            label={getFieldError(val)}
                            aria-required="true"
                            aria-label={`${UserDataKorean[val]} 입력`}
                        />
                        <div className="mt-5" />
                    </div>
                ))}
            </div>

            <Button type="submit" className="h-12 w-full text-xl">
                회원가입
            </Button>
        </form>
    );
};

export default SignUpForm;
