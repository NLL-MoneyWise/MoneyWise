import { defaultApi } from './../common/util/api';
import { AxiosInstance } from 'axios';
import { UploadResponse } from './types/reponse/index';

interface UploadRepository {
    upload(): Promise<UploadResponse>;
}

export class UploadRepositoryImpl implements UploadRepository {
    private static instance: UploadRepositoryImpl | null = null;
    private api: AxiosInstance;

    private constructor() {
        this.api = defaultApi();
    }

    public static getInstance(): UploadRepositoryImpl {
        if (!UploadRepositoryImpl.instance) {
            UploadRepositoryImpl.instance = new UploadRepositoryImpl();
        }
        return UploadRepositoryImpl.instance;
    }

    async upload(): Promise<UploadResponse> {
        const { data } = await this.api.get('/upload/presigned-url');
        return data;
    }
}
