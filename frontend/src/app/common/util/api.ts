import { RefreshValidateResponse } from './../../(auth)/types/reponse/reponse-validate';
import axios from 'axios';
import type { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import { CustomError, ErrorType } from '../types/error/error';
import {
    getAccessToken,
    saveAccessToken,
    removeAccessToken
} from '@/app/(auth)/util/toekn';

const baseUrl = process.env.NEXT_PUBLIC_API_URL;

export const defaultApi = (
    option?: InternalAxiosRequestConfig
): AxiosInstance => {
    const instance = axios.create({
        baseURL: baseUrl,
        ...option,
        withCredentials: true
    });

    instance.interceptors.request.use(
        async (config) => {
            try {
                const token = await getAccessToken();
                if (token) {
                    config.headers['Authorization'] = `Bearer ${token}`;
                }
            } catch (error) {
                console.error('토큰 가져오기 실패:', error);
            }
            return config;
        },
        (error) => {
            const errorMessage =
                error.response?.data?.message || '알 수 없는 에러입니다.';
            const errorStatus = error.response?.status || 500;
            const errorType = error.response?.data?.typeName || 'UNKNOWN';

            return Promise.reject(
                new CustomError(errorMessage, errorStatus, errorType)
            );
        }
    );

    instance.interceptors.response.use(
        (response) => {
            return response;
        },
        async (error) => {
            const originalRequest = error.config;

            if (error.response?.status === 401 && !originalRequest._retry) {
                originalRequest._retry = true;

                try {
                    const response = await fetch(`${baseUrl}/auth/refresh`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        credentials: 'include'
                    });

                    if (!response.ok) {
                        window.location.replace(
                            'http://localhost:3000/login?error=token_expired'
                        );
                        await removeAccessToken();

                        return Promise.reject(
                            new CustomError(
                                '세션이 만료됬습니다.',
                                401,
                                ErrorType.API
                            )
                        );
                    }
                    const data: RefreshValidateResponse = await response.json();
                    await saveAccessToken(data.access_token);
                    originalRequest.headers['Authorization'] =
                        `Bearer ${data.access_token}`;
                    return instance(originalRequest);
                } catch (refreshError) {
                    console.log(refreshError);
                }
            }
            const errorMessage =
                error.response?.data?.message || '알 수 없는 에러입니다.';
            const errorStatus = error.response?.status || 500;
            const errorType = error.response?.data?.typeName || 'UNKNOWN';
            return Promise.reject(
                new CustomError(errorMessage, errorStatus, errorType)
            );
        }
    );

    return instance;
};
