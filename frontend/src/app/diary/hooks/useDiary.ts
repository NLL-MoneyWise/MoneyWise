import { MemoRequest, PutMemoRequest } from './../types/request/request-memo';
import {
    SaveMemoResponse,
    GetMemoResponse
} from './../types/response/reponse-memo';
import { useMutation, useQuery } from '@tanstack/react-query';
import { DiaryRepositoryImpl } from '../repository';
import { useToastStore } from '@/app/common/hooks/useToastStore';

const useDiary = () => {
    const diaryRepositoryImpl = DiaryRepositoryImpl.getInstance();
    const { addToast } = useToastStore();

    const saveMemo = useMutation<SaveMemoResponse, Error, MemoRequest>({
        mutationFn: diaryRepositoryImpl.saveMemo.bind(diaryRepositoryImpl),
        onSuccess: async (data) => {
            addToast(data.message, 'success');
        }
    });

    const getMemo = useQuery<GetMemoResponse, Error>({
        queryKey: ['memo'],
        queryFn: diaryRepositoryImpl.getMemo.bind(diaryRepositoryImpl)
    });

    const editMemo = useMutation<SaveMemoResponse, Error, PutMemoRequest>({
        mutationFn: diaryRepositoryImpl.editMemo.bind(diaryRepositoryImpl),
        onSuccess: async (data) => {
            addToast(data.message, 'success');
        }
    });

      // 메모 삭제하기
      async deleteMemo(date: DeleteMemoRequest): Promise<DeleteResponse> {
        const { data } = await this.api.delete(`/memos/delete`, {
            data: date
        });

        return data;
    }
    return {
        saveMemo,
        getMemo,
        editMemo
    };
};

export default useDiary;
