import { defaultApi } from '../common/util/api';
import { AxiosInstance } from 'axios';
import { MemoRequest } from './types/request';
import { saveMemoResponse } from './types/response';

interface DiaryRepository {
    saveMemo(memo: MemoRequest): Promise<saveMemoResponse>;
}

export class DiaryRepositoryImpl implements DiaryRepository {
    private static instance: DiaryRepositoryImpl | null = null;

    private api: AxiosInstance;

    private constructor() {
        this.api = defaultApi();
    }

    public static getInstance(): DiaryRepositoryImpl {
        if (!DiaryRepositoryImpl.instance) {
            DiaryRepositoryImpl.instance = new DiaryRepositoryImpl();
        }
        return DiaryRepositoryImpl.instance;
    }

    // 메모 저장
    async saveMemo(memo: MemoRequest): Promise<saveMemoResponse> {
        const { data } = await this.api.post('/memos/save', memo);
        return data;
    }
}
