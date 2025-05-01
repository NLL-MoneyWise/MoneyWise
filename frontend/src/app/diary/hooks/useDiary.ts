import {
    MemoRequest,
    DeleteMemoRequest
} from './../types/request/request-memo';
import {
    SaveMemoResponse,
    GetMemoResponse,
    DeleteResponse
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
        queryFn: diaryRepositoryImpl.getMemo.bind(diaryRepositoryImpl),
        staleTime: 60000,
        gcTime: 900000
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
