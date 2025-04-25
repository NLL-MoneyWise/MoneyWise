import { MemoRequest } from './../types/request/request-memo';
import { saveMemoResponse } from './../types/response/reponse-memo';
import { useMutation } from '@tanstack/react-query';
import { DiaryRepositoryImpl } from '../repository';
import { useToastStore } from '@/app/common/hooks/useToastStore';

const useDiary = () => {
    const diaryRepositoryImpl = DiaryRepositoryImpl.getInstance();
    const { addToast } = useToastStore();

    const saveMemo = useMutation<saveMemoResponse, Error, MemoRequest>({
        mutationFn: diaryRepositoryImpl.saveMemo.bind(diaryRepositoryImpl),
        onSuccess: (data) => {
            addToast(data.message, 'success');
        }
    });

    return {
        saveMemo
    };
};

export default useDiary;
