import { LoginRequest } from '../types/request/index';
import { useState } from 'react';
import { z as zod } from 'zod';

const loginSchema = zod.object({
    email: zod
        .string()
        .email('유효한 이메일을 입력하세요')
        .min(1, '이메일을 입력하세요'),
    password: zod.string().min(8, '비밀번호는 8자 이상 입력하세요')
});

/**
 * 회원정보를 검증합니다.
 */

const useValidateForm = () => {
    const [emailError, setEmailError] = useState<string>('');
    const [passwordError, setPasswordError] = useState<string>('');
    const [isValid, setIsValide] = useState<boolean>(false);

    const ValidateForm = ({ email, password }: LoginRequest) => {
        try {
            loginSchema.parse({ email, password });
            setIsValide(true);
        } catch (error) {
            if (error instanceof zod.ZodError) {
                const errorMessages = error.errors.map((err) => err.message);

                setEmailError(
                    errorMessages.find((msg) => msg.includes('이메일')) || ''
                );
                setPasswordError(
                    errorMessages.find((msg) => msg.includes('비밀번호')) || ''
                );
            } else {
                throw new Error('아이디 비밀번호 검증에 실패했습니다.');
            }
        }
    };

    return { ValidateForm, emailError, passwordError, isValid };
};

export default useValidateForm;
