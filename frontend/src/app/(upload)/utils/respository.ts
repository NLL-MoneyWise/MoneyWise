import {
    UploadReciptResponse,
    AnalyzeReceiptResponse
} from '../types/reponse/index';
import { AnalyzeReceiptRequest } from '../types/request/index';
import { defaultApi } from '@/app/common/util/api';
import { AxiosInstance } from 'axios';

interface UploadRepository {
    upload(): Promise<UploadReciptResponse>;
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

    async upload(): Promise<UploadReciptResponse> {
        const { data } = await this.api.get('/upload/presigned-url');
        return data;
    }

    async analyze({
        accessUrl
    }: AnalyzeReceiptRequest): Promise<AnalyzeReceiptResponse> {
        const { data } = await this.api.post('/workflows/receipt-process', {
            accessUrl
        });
        return data;
    }
}
