import { useToastStore } from './../../common/hooks/useToastStore';
import { PostIncomeRequest } from './../types/request/request-income';
import {
    useMutation,
    useQuery,
    useQueryClient,
    useSuspenseQuery
} from '@tanstack/react-query';
import { ConsumptionRepositoryImpl } from '../util/respository';
import {
    PostFixedConsumptionResponse,
    PostIncomeResponse
} from '../types/reponse';
import { PostFixedConsumptionRequest } from '../types/request';

const useCounsumption = () => {
    const counsumptionRepository = ConsumptionRepositoryImpl.getInstance();
    const addToast = useToastStore((state) => state.addToast);
    const queryClient = useQueryClient();

    const getAllIncome = useSuspenseQuery({
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

    const PostFixedConsumption = useMutation<
        PostFixedConsumptionResponse,
        Error,
        PostFixedConsumptionRequest
    >({
        mutationFn: counsumptionRepository.postFiexdConsumption.bind(
            counsumptionRepository
        ),
        onSuccess: (data) => {
            addToast(data.message, 'success');
            queryClient.invalidateQueries({ queryKey: ['income'] });
        }
    });

    const getFiexedCost = useQuery({
        queryKey: ['income'],
        queryFn: () => counsumptionRepository.getAllFiexedCost(),
        staleTime: 60000,
        gcTime: 900000,
        throwOnError: true
    });

    return { getAllIncome, getFiexedCost, postIncome, PostFixedConsumption };
};

export default useCounsumption;
