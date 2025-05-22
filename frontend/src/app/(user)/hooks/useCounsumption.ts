import { useQuery } from '@tanstack/react-query';
import { ConsumptionRepositoryImpl } from '../util/respository';

const useCounsumption = (accessUrl?: string) => {
    const counsumptionRepository = ConsumptionRepositoryImpl.getInstance();

    const getAllIncome = useQuery({
        queryKey: ['income'],
        queryFn: () => counsumptionRepository.getAllIncome(),
        staleTime: 60000,
        gcTime: 900000,
        throwOnError: true
    });

    const getFiexedCost = useQuery({
        queryKey: ['income'],
        queryFn: () => counsumptionRepository.getAllFiexedCost(),
        staleTime: 60000,
        gcTime: 900000,
        throwOnError: true
    });

    return { getAllIncome, getFiexedCost };
};

export default useCounsumption;
