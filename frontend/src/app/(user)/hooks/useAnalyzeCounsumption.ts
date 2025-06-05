import { useQuery } from '@tanstack/react-query';
import { ConsumptioneResponse } from '../types/reponse/response-consumptione';
import { ConsumptionRepositoryImpl } from '../util/respository';
import { ConsumptionRequest } from './../types/request/requset-consumptione';

const useAnalyzeConsumption = (
    analyzeConsumptionParams: ConsumptionRequest
) => {
    const counsumptionRepository = ConsumptionRepositoryImpl.getInstance();

    return useQuery<ConsumptioneResponse, Error>({
        queryKey: ['consumption', 'analyze', analyzeConsumptionParams],
        queryFn: () => {
            return counsumptionRepository.analyeConsumption(
                analyzeConsumptionParams
            );
        },

        staleTime: 60000,
        gcTime: 900000,
        throwOnError: true
    });
};

export default useAnalyzeConsumption;
