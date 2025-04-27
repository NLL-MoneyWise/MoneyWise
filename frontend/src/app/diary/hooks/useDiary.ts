import { MemoRequest } from './../types/request/request-memo';
import {
    SaveMemoResponse,
    GetMemoResponse
} from './../types/response/reponse-memo';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
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

    return {
        saveMemo,
        getMemo
    };
};

export default useDiary;
