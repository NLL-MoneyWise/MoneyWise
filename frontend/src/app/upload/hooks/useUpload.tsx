import { useQuery } from '@tanstack/react-query';
import { UploadRepositoryImpl } from '../respository';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { UploadResponse } from '../types/reponse/index';

const useUpload = () => {
    const uploadRepositoryImpl = UploadRepositoryImpl.getInstance();
    const { addToast } = useToastStore();

    const uploadRecipt = useQuery<UploadResponse, Error>({
        queryKey: ['recipt'],
        queryFn: uploadRepositoryImpl.upload.bind(uploadRepositoryImpl)
    });

    return {
        uploadRecipt
    };
};

export default useUpload;
