import { useToastStore } from '../../common/hooks/useToastStore';
import {
    PostIncomeRequest,
    DeleteIncomeRequest
} from '../types/request/request-income';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { ConsumptionRepositoryImpl } from '../util/respository';
import { PostIncomeResponse, DeleteIncomeResponse } from '../types/reponse';

const useIncome = () => {
    const counsumptionRepository = ConsumptionRepositoryImpl.getInstance();
    const addToast = useToastStore((state) => state.addToast);
    const queryClient = useQueryClient();

    const getAllIncome = useQuery({
        queryKey: ['income'],
        queryFn: () => counsumptionRepository.getAllIncome(),
        staleTime: 60000,
        gcTime: 900000
    });

    const postIncome = useMutation<
        PostIncomeResponse,
        Error,
        PostIncomeRequest
    >({
        mutationFn: counsumptionRepository.postIncome.bind(
            counsumptionRepository
        ),
        onSuccess: (data) => {
            addToast(data.message, 'success');
            queryClient.invalidateQueries({ queryKey: ['income'] });
        }
    });

    const deleteIncome = useMutation<
        DeleteIncomeResponse,
        Error,
        DeleteIncomeRequest
    >({
        mutationFn: counsumptionRepository.deleteIncome.bind(
            counsumptionRepository
        ),
        onSuccess: (data) => {
            addToast(data.message, 'success');
            queryClient.invalidateQueries({ queryKey: ['income'] });
        }
    });

    return { getAllIncome, postIncome, deleteIncome };
};

export default useIncome;
