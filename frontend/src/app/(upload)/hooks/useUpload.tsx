import { useMutation, useQuery } from '@tanstack/react-query';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import {
    UploadReciptResponse,
    AnalyzeReceiptResponse
} from '../types/reponse/index';
import { UploadRepositoryImpl } from '../utils/respository';
import { AnalyzeReceiptRequest } from '../types/request/index';
import { useRouter } from 'next/navigation';

const useUpload = () => {
    const uploadRepositoryImpl = UploadRepositoryImpl.getInstance();
    const { addToast } = useToastStore();
    const router = useRouter();

    const uploadRecipt = useQuery<UploadReciptResponse, Error>({
        queryKey: ['recipt'],
        queryFn: uploadRepositoryImpl.upload.bind(uploadRepositoryImpl)
    });

    const analyzeRecipt = useMutation<
        AnalyzeReceiptResponse,
        Error,
        AnalyzeReceiptRequest
    >({
        mutationFn: uploadRepositoryImpl.analyze.bind(uploadRepositoryImpl),
        onSuccess: async () => {
            addToast('영수증 분석이 완료되었습니다.', 'success');
            router.push('/');
        }
    });

    return {
        uploadRecipt,
        analyzeRecipt
    };
};

export default useUpload;
