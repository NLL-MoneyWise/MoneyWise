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
        confirmPassword: zod.string()
    })
    .refine((data) => data.password === data.confirmPassword, {
        message: '비밀번호가 일치하지 않습니다.',
        path: ['confirmPassword']
    });

const useValidateForm = () => {
    const [errors, setErrors] = useState<Record<string, string>>({});

    const validateForm = (
        data: LoginRequest & { confirmPassword?: string }
    ) => {
        try {
            loginSchema.parse(data);
            setErrors({});
            return true;
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
                return false;
            }
            throw error;
        }
    };

    const getFieldError = (field: string) => errors[field] || '';

    const hasFieldError = (field: string) => Boolean(errors[field]);

    return {
        validateForm,
        getFieldError,
        hasFieldError
    };
};

export default useValidateForm;
