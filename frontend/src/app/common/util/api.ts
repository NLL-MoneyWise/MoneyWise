import axios from 'axios';
import type { AxiosInstance, InternalAxiosRequestConfig } from 'axios';

const baseUrl = process.env.API_URL as string;

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
            return Promise.reject(error);
        }
    );

    return instance;
};
