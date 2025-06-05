import {
    useSuspenseQuery,
    useMutation,
    useQueryClient,
    useQuery
} from '@tanstack/react-query';
import { useToastStore } from '@/app/common/hooks/useToastStore';
import { ConsumptionRepositoryImpl } from '../util/respository';
import {
    PostFixedConsumptionResponse,
    DeleteFixedConsumptionResponse
} from '../types/reponse';
import {
    PostFixedConsumptionRequest,
    DeleteFixedConsumptionRequest
} from '../types/request';

const useFiexCost = () => {
    const counsumptionRepository = ConsumptionRepositoryImpl.getInstance();
    const addToast = useToastStore((state) => state.addToast);
    const queryClient = useQueryClient();

    const getFiexedCost = useQuery({
        queryKey: ['fixed-cost'],
        queryFn: () => counsumptionRepository.getAllFiexedCost(),
        staleTime: 60000,
        gcTime: 900000
    });

    const postFixedConsumption = useMutation<
        PostFixedConsumptionResponse,
        Error,
        PostFixedConsumptionRequest
    >({
        mutationFn: counsumptionRepository.postFiexdConsumption.bind(
            counsumptionRepository
        ),
        onSuccess: (data) => {
            addToast(data.message, 'success');
            queryClient.invalidateQueries({ queryKey: ['fixed-cost'] });
        }
    });

    return { getFiexedCost, postFixedConsumption };
};

export default useFiexCost;
