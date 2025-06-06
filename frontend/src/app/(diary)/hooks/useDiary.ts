import {
    MemoRequest,
    DeleteMemoRequest
} from './../types/request/request-memo';
import {
    SaveMemoResponse,
    GetMemoResponse,
    DeleteResponse
} from './../types/response/reponse-memo';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { DiaryRepositoryImpl } from '../util/repository';

const useDiary = () => {
    const diaryRepositoryImpl = DiaryRepositoryImpl.getInstance();
    const addToast = useToastStore((state) => state.addToast);

    const queryClient = useQueryClient();

    const getMemo = useQuery<GetMemoResponse, Error>({
        queryKey: ['memo'],
        queryFn: diaryRepositoryImpl.getMemo.bind(diaryRepositoryImpl),
        staleTime: 60,
        gcTime: 300
    });

    const createMemo = useMutation<SaveMemoResponse, Error, MemoRequest>({
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
            queryClient.invalidateQueries({ queryKey: ['memo'] });
        }
    });

    const deleteMemo = useMutation<DeleteResponse, Error, DeleteMemoRequest>({
        mutationFn: diaryRepositoryImpl.deleteMemo.bind(diaryRepositoryImpl),
        onSuccess: async (data) => {
            addToast(data.message, 'success');
            queryClient.invalidateQueries({ queryKey: ['memo'] });
        }
    });

    return {
        createMemo,
        getMemo,
        editMemo,
        deleteMemo
    };
};

export default useDiary;
