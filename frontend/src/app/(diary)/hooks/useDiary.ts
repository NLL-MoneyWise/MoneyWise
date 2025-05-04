import {
    MemoRequest,
    DeleteMemoRequest
} from './../types/request/request-memo';
import {
    SaveMemoResponse,
    GetMemoResponse,
    DeleteResponse
} from './../types/response/reponse-memo';
import { QueryClient, useMutation, useQuery } from '@tanstack/react-query';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { DiaryRepositoryImpl } from '../util/repository';

const useDiary = () => {
    const diaryRepositoryImpl = DiaryRepositoryImpl.getInstance();
    const { addToast } = useToastStore();

    const queryClient = new QueryClient();

    const getMemo = useQuery<GetMemoResponse, Error>({
        queryKey: ['memo'],
        queryFn: diaryRepositoryImpl.getMemo.bind(diaryRepositoryImpl),
        staleTime: 60000,
        gcTime: 900000
    });

    const saveMemo = useMutation<SaveMemoResponse, Error, MemoRequest>({
        mutationFn: diaryRepositoryImpl.saveMemo.bind(diaryRepositoryImpl),
        onSuccess: async (data) => {
            addToast(data.message, 'success');
            queryClient.invalidateQueries({ queryKey: ['memo'] });
        }
    });

    const editMemo = useMutation<SaveMemoResponse, Error, MemoRequest>({
        mutationFn: diaryRepositoryImpl.editMemo.bind(diaryRepositoryImpl),
        onSuccess: async (data) => {
            addToast(data.message, 'success');
        }
    });

    const deleteMemo = useMutation<DeleteResponse, Error, DeleteMemoRequest>({
        mutationFn: diaryRepositoryImpl.deleteMemo.bind(diaryRepositoryImpl),
        onSuccess: async (data) => {
            addToast(data.message, 'success');
        }
    });

    return {
        saveMemo,
        getMemo,
        editMemo,
        deleteMemo
    };
};

export default useDiary;
