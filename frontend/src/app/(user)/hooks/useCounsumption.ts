import { ConsumptioneResponse } from '../types/reponse/response-consumptione';
import { useQuery } from '@tanstack/react-query';
import { ConsumptionRepositoryImpl } from '../util/respository';
import { ConsumptionRequest } from './../types/request/requset-consumptione';

const useCounsumption = (params: ConsumptionRequest) => {
    const counsumptionRepository = ConsumptionRepositoryImpl.getInstance();

    const analyzeCounsumption = useQuery<ConsumptioneResponse, Error>({
        queryKey: ['counsumption', params],
        queryFn: () => {
            return counsumptionRepository.analyeConsumption.bind(
                counsumptionRepository
            )(params);
        },
        staleTime: 60000,
        gcTime: 900000,
        throwOnError: true
    });

    return { analyzeCounsumption };
};

export default useCounsumption;
