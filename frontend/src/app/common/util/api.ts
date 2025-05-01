import axios from 'axios';
import type { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import { CustomError } from '../types/error/error';
import {
    getAccessToken,
    saveAccessToken,
    removeAccessToken
} from '@/app/auth/util/toekn';
import { RefreshValidateResponse } from './../../auth/types/reponse/reponse-validate';

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
            const token = await getAccessToken();
            if (token) {
                config.headers['Authorization'] = `Bearer ${token}`;
            }
            return config;
        },
        (error) => {
            return Promise.reject(error);
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
                        }
                    });

                    if (!response.ok) {
                        throw new Error('토큰 갱신 실패');
                    }

                    const data: RefreshValidateResponse = await response.json();

                    error.config.headers['Authorization'] =
                        `Bearer ${data.access_token}`;

                    await saveAccessToken(data.access_token);

                    return instance(originalRequest);
                } catch (refreshError) {
                    const errorMessage =
                        error.response?.data?.message ||
                        '알 수 없는 에러입니다.';
                    const errorType =
                        error.response.data?.typeName || 'UNKNOWN';
                    const errorStatus = error.response?.status || 500;

                    await removeAccessToken();

                    window.location.href = `/login?callbackUrl=${window.location.pathname}&error=token_expired`;

                    return Promise.reject(
                        new CustomError(errorMessage, errorStatus, errorType)
                    );
                }
            } else {
                const errorMessage =
                    error.response?.data?.message || '알 수 없는 에러입니다.';
                const errorType = error.response.data?.typeName || 'UNKNOWN';
                const errorStatus = error.response?.status || 500;

                return Promise.reject(
                    new CustomError(errorMessage, errorStatus, errorType)
                );
            }
        }
    );

    return instance;
};
