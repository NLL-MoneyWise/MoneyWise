import { defaultApi } from './../../common/util/api';
import { AxiosInstance } from 'axios';
import { MemoRequest, DeleteMemoRequest } from '../types/request';
import {
    SaveMemoResponse,
    GetMemoResponse,
    DeleteResponse
} from '../types/response';

interface DiaryRepository {
    saveMemo(memo: MemoRequest): Promise<SaveMemoResponse>;
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
    async saveMemo(memo: MemoRequest): Promise<SaveMemoResponse> {
        const { data } = await this.api.post('/memos/save', memo);
        return data;
    }

    // 메모 불러오기
    async getMemo(): Promise<GetMemoResponse> {
        const { data } = await this.api.get('/memos/find');

        return data;
    }

    // 메모 수정하기
    async editMemo(memo: MemoRequest): Promise<SaveMemoResponse> {
        const { data } = await this.api.put('/memos/update', memo);

        return data;
    }

    // 메모 삭제하기
    async deleteMemo(date: DeleteMemoRequest): Promise<DeleteResponse> {
        const { data } = await this.api.delete(`/memos/delete`, {
            data: date
        });

        return data;
    }
}
