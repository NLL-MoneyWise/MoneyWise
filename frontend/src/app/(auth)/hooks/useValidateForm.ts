import { LoginRequest } from '../types/request/index';
import { useState } from 'react';
import { z as zod } from 'zod';

const loginSchema = zod
    .object({
        email: zod
            .string()
            .email('유효한 이메일을 입력하세요')
            .min(1, '이메일을 입력하세요'),
        password: zod.string().min(8, '비밀번호는 8자 이상 입력하세요'),
        confirmpassword: zod.string()
    })
    .refine((data) => data.password === data.confirmpassword, {
        message: '비밀번호가 일치하지 않습니다.',
        path: ['confirmpassword']
    });

const useValidateForm = () => {
    const [errors, setErrors] = useState<Record<string, string>>({});
    const [isValid, setIsValid] = useState<boolean>(false);

    /**
     * 검증을 통해 올바른 값을 입력했는지 확인 후 참/거짓을 반환합니다.
     * @returns
     */
    const validateForm = (
        data: LoginRequest & { confirmpassword?: string }
    ): void => {
        try {
            loginSchema.parse(data);
            setErrors({});
            setIsValid(true);
        } catch (error) {
            if (error instanceof zod.ZodError) {
                const newErrors = error.errors.reduce(
                    (acc, err) => {
                        const field = err.path[0];
                        acc[field] = err.message;
                        return acc;
                    },
                    {} as Record<string, string>
                );

                setErrors(newErrors);
                setIsValid(false);
            }
        }
    };

    const getFieldError = (field: string) => errors[field] || '';

    return {
        validateForm,
        getFieldError,
        isValid
    };
};

export default useValidateForm;
