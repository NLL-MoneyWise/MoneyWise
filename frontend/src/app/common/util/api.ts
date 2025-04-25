import axios from 'axios';
import type { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import { CustomError } from '../types/error/error';
import { getAccessToken } from '@/app/auth/util/toekn';

const baseUrl = process.env.NEXT_PUBLIC_API_URL;

export const defaultApi = (
    option?: InternalAxiosRequestConfig
): AxiosInstance => {
    const instance = axios.create({
        baseURL: baseUrl,
        ...option
    });

    instance.interceptors.request.use(
        async (config) => {
            const token = await getAccessToken();
            if (token) {
                config.headers['Authorization'] = `Bearer ${token}`;
            }
            return config;
        },
        (error) => {
            console.error('토큰 가져오기 실패:', error);

            return Promise.reject(error);
        }
    );

    instance.interceptors.response.use(
        (response) => {
            return response;
        },
        (error) => {
            const errorMessage =
                error.response?.data?.message || '알 수 없는 에러입니다.';
            const errorType = error.response.data?.typeName || 'UNKNOWN';
            const errorStatus = error.response?.status || 500;

            return Promise.reject(
                new CustomError(errorMessage, errorStatus, errorType)
            );
        }
    );

    return instance;
};
