import axios from 'axios';
import type { AxiosInstance, InternalAxiosRequestConfig } from 'axios';
import { CustomError } from '../types/error/error';

const baseUrl = process.env.NEXT_PUBLIC_API_URL;

export const defaultApi = (
    option?: InternalAxiosRequestConfig
): AxiosInstance => {
    const instance = axios.create({
        baseURL: baseUrl,
        ...option
    });

    instance.interceptors.request.use(
        (config) => {
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
